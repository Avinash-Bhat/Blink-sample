package com.sample.blinkid;

import com.microblink.activity.ScanActivity;
import com.microblink.activity.ShowOcrResultMode;
import com.microblink.geometry.Rectangle;
import com.microblink.metadata.MetadataSettings;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkocr.BlinkOCRRecognitionResult;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.microblink.activity.BaseScanActivity.EXTRAS_ALLOW_PINCH_TO_ZOOM;
import static com.microblink.activity.BaseScanActivity.EXTRAS_BEEP_RESOURCE;
import static com.microblink.activity.BaseScanActivity.EXTRAS_IMAGE_LISTENER;
import static com.microblink.activity.BaseScanActivity.EXTRAS_IMAGE_METADATA_SETTINGS;
import static com.microblink.activity.BaseScanActivity.EXTRAS_LICENSE_KEY;
import static com.microblink.activity.BaseScanActivity.EXTRAS_RECOGNITION_RESULTS;
import static com.microblink.activity.BaseScanActivity.EXTRAS_RECOGNITION_SETTINGS;
import static com.microblink.activity.BaseScanActivity.EXTRAS_SHOW_FOCUS_RECTANGLE;
import static com.microblink.activity.ScanActivity.EXTRAS_SHOW_OCR_RESULT;
import static com.microblink.activity.ScanActivity.EXTRAS_SHOW_OCR_RESULT_MODE;

public abstract class Scanner extends BaseObservable {

    private static final AtomicInteger SCAN_CODE_COUNTER = new AtomicInteger(1);

    private final int scanCode = SCAN_CODE_COUNTER.getAndIncrement();

    public final ObservableBoolean detectDewrappedOnly = new ObservableBoolean(true);

    @Nullable
    private ScanResult result;

    private final Fragment fragment;

    Scanner(Fragment fragment) {
        this.fragment = fragment;
    }

    @Nullable
    @Bindable
    public ScanResult getResult() {
        return result;
    }

    private void setResult(@Nullable ScanResult result) {
        this.result = result;
        notifyPropertyChanged(com.sample.blinkid.BR.result);
    }

    static int getDewrapedHeight(float relHeight) {
        return (int) Math.ceil(relHeight * 1000);
    }

    @NonNull
    static Rectangle getRectangle(float left, float right, float top, float bottom, float width,
            float height) {
        return new Rectangle(left / width, top / height, (right - left) / width,
                (bottom - top) / height);
    }

    public void startScan() {
        Intent intent = new Intent(fragment.getContext(), ScanActivity.class);
        RecognitionSettings settings = new RecognitionSettings();
        settings.setRecognizerSettingsArray(getRecognizerSettings());
        settings.setNumMsBeforeTimeout(2000);
        intent.putExtra(EXTRAS_BEEP_RESOURCE, R.raw.beep);
        intent.putExtra(EXTRAS_LICENSE_KEY,
                "COJWWC5O-G3KBDEA6-DDKU2KAI-KJG5BAGC-LBU5SVJO-XDTGW77S-UQU7H7OZ-LXOJNRTS");
        intent.putExtra(EXTRAS_SHOW_OCR_RESULT, true);
        intent.putExtra(EXTRAS_SHOW_FOCUS_RECTANGLE, true);
        intent.putExtra(EXTRAS_ALLOW_PINCH_TO_ZOOM, true);
        intent.putExtra(EXTRAS_SHOW_OCR_RESULT_MODE, (Parcelable) ShowOcrResultMode.STATIC_CHARS);
        MetadataSettings.ImageMetadataSettings ims = new MetadataSettings.ImageMetadataSettings();
        boolean onlyDewrapped = detectDewrappedOnly.get();
        if (onlyDewrapped) {
            ims.setDewarpedImageEnabled(true);
        } else {
            ims.setSuccessfulScanFrameEnabled(true);
        }
        intent.putExtra(EXTRAS_IMAGE_METADATA_SETTINGS, ims);
        intent.putExtra(EXTRAS_IMAGE_LISTENER, new BroadcastingImageListener(onlyDewrapped,
                scanCode));
        intent.putExtra(EXTRAS_RECOGNITION_SETTINGS, settings);
        fragment.startActivityForResult(intent, scanCode);
        BlinkApplication.getApplication().setDetectedImage(scanCode, null);
    }

    @NonNull
    protected abstract RecognizerSettings[] getRecognizerSettings();

    boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (scanCode == requestCode) {
            if (resultCode == RESULT_OK && data != null) {
                Bundle extras = data.getExtras();
                RecognitionResults results = extras.getParcelable(EXTRAS_RECOGNITION_RESULTS);
                if (results != null) {
                    BaseRecognitionResult[] recognitionResults = results.getRecognitionResults();
                    if (recognitionResults != null) {
                        Timber.d("results: %s", Arrays.toString(recognitionResults));
                        for (int i = 0; i < recognitionResults.length; i++) {
                            BaseRecognitionResult result = recognitionResults[i];
                            Timber.v("result[%d]: %s", i, result);
                            if (result.isValid() && !result.isEmpty()) {
                                if (result instanceof BlinkOCRRecognitionResult) {
                                    BlinkOCRRecognitionResult blinkResult
                                            = (BlinkOCRRecognitionResult) result;
                                    onResult(blinkResult);
                                }
                            } else {
                                Toast.makeText(fragment.getContext(),
                                        "Couldnt recognize the details", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    protected abstract void onResult(BlinkOCRRecognitionResult result);

    void onImageDetected() {
        Bitmap bitmap = BlinkApplication.getApplication().getDetectedImage(scanCode);
        if (bitmap == null) {
            return;
        }
        if (result == null) {
            setResult(new ScanResult());
        }
        result.detectedImage.set(new BitmapDrawable(fragment.getResources(), bitmap));
    }
}
