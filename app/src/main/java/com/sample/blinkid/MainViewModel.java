package com.sample.blinkid;

import com.microblink.activity.ScanActivity;
import com.microblink.activity.ShowOcrResultMode;
import com.microblink.recognizers.settings.RecognitionSettings;
import com.microblink.recognizers.settings.RecognizerSettings;

import android.content.Intent;
import android.os.Parcelable;

import static com.microblink.activity.BaseScanActivity.EXTRAS_BEEP_RESOURCE;
import static com.microblink.activity.ScanActivity.EXTRAS_ALLOW_PINCH_TO_ZOOM;
import static com.microblink.activity.ScanActivity.EXTRAS_LICENSE_KEY;
import static com.microblink.activity.ScanActivity.EXTRAS_RECOGNITION_SETTINGS;
import static com.microblink.activity.ScanActivity.EXTRAS_SHOW_FOCUS_RECTANGLE;
import static com.microblink.activity.ScanActivity.EXTRAS_SHOW_OCR_RESULT;
import static com.microblink.activity.ScanActivity.EXTRAS_SHOW_OCR_RESULT_MODE;

public class MainViewModel {

    private final MainActivity activity;

    MainViewModel(MainActivity activity) {
        this.activity = activity;
    }

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
        activity.startActivity(intent);
    }
}