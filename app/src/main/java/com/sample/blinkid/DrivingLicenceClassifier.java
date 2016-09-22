package com.sample.blinkid;

import com.microblink.recognizers.blinkocr.BlinkOCRRecognitionResult;
import com.microblink.recognizers.blinkocr.DocumentClassifier;

import android.os.Parcel;
import android.text.TextUtils;

public class DrivingLicenceClassifier implements DocumentClassifier {

    static final String ID_NAME = "Name";

    @Override
    public String classifyDocument(BlinkOCRRecognitionResult result) {
        String parsedId = result.getParsedResult(ID_NAME, ID_NAME);
        if (!TextUtils.isEmpty(parsedId)) {
            return ID_NAME;
        }
        return null;
    }

    public static final Creator<DrivingLicenceClassifier> CREATOR
            = new Creator<DrivingLicenceClassifier>() {
        @Override
        public DrivingLicenceClassifier createFromParcel(Parcel source) {
            return new DrivingLicenceClassifier();
        }

        @Override
        public DrivingLicenceClassifier[] newArray(int size) {
            return new DrivingLicenceClassifier[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
