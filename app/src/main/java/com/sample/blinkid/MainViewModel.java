package com.sample.blinkid;

import com.microblink.activity.ScanActivity;
import com.microblink.activity.ShowOcrResultMode;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkocr.BlinkOCRRecognitionResult;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.Arrays;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.microblink.activity.BaseScanActivity.EXTRAS_BEEP_RESOURCE;
import static com.microblink.activity.BaseScanActivity.EXTRAS_RECOGNITION_RESULTS;
import static com.microblink.activity.ScanActivity.EXTRAS_ALLOW_PINCH_TO_ZOOM;
import static com.microblink.activity.ScanActivity.EXTRAS_LICENSE_KEY;
import static com.microblink.activity.ScanActivity.EXTRAS_RECOGNITION_SETTINGS;
import static com.microblink.activity.ScanActivity.EXTRAS_SHOW_FOCUS_RECTANGLE;
import static com.microblink.activity.ScanActivity.EXTRAS_SHOW_OCR_RESULT;
import static com.microblink.activity.ScanActivity.EXTRAS_SHOW_OCR_RESULT_MODE;
import static com.sample.blinkid.SixdeeIdClassifier.ID_NAME;

public class MainViewModel {

    private static final int REQUEST_SCAN = 1;

    private final MainActivity activity;

    MainViewModel(MainActivity activity) {
        this.activity = activity;
    }

    public final ObservableField<String> fullName = new ObservableField<>();

    public void scanID() {
        Intent intent = new Intent(activity, ScanActivity.class);
        RecognitionSettings settings = new RecognitionSettings();
        settings.setRecognizerSettingsArray(new RecognizerSettings[]{
                Recognizers.getInstance().sixdeeId()
        });
        settings.setNumMsBeforeTimeout(2000);
        intent.putExtra(EXTRAS_BEEP_RESOURCE, R.raw.beep);
        intent.putExtra(EXTRAS_LICENSE_KEY,
                "DYKXIPR7-MVMWD3VR-EOSFOFM6-5VX7LY26-5PKANK2P-LSJKUUSN-2CAMFWDC-2D6LJG6H");
        intent.putExtra(EXTRAS_SHOW_OCR_RESULT, true);
        intent.putExtra(EXTRAS_SHOW_FOCUS_RECTANGLE, true);
        intent.putExtra(EXTRAS_ALLOW_PINCH_TO_ZOOM, true);
        intent.putExtra(EXTRAS_SHOW_OCR_RESULT_MODE, (Parcelable) ShowOcrResultMode.STATIC_CHARS);
        intent.putExtra(EXTRAS_RECOGNITION_SETTINGS, settings);
        activity.startActivityForResult(intent, REQUEST_SCAN);
    }

    boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_SCAN == requestCode) {
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
                                    String name = blinkResult.getParsedResult(ID_NAME, ID_NAME);
                                    Timber.v("parsed result: %s", name);
                                    fullName.set(name);
                                }
                            } else {
                                Toast.makeText(activity, "Couldnt recognize the details",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}