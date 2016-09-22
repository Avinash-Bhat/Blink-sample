package com.sample.blinkid;

import com.microblink.recognizers.blinkocr.BlinkOCRRecognitionResult;
import com.microblink.recognizers.blinkocr.DocumentClassifier;

import android.os.Parcel;
import android.text.TextUtils;

class PassportClassifier implements DocumentClassifier {

    static final String ID_NAME = "Name";

    PassportClassifier() {
    }

    @Override
    public String classifyDocument(BlinkOCRRecognitionResult result) {
        String parsedId = result.getParsedResult(ID_NAME, ID_NAME);
        if (!TextUtils.isEmpty(parsedId)) {
            return ID_NAME;
        }
        return null;
    }

    public static final Creator<PassportClassifier> CREATOR
            = new Creator<PassportClassifier>() {
        @Override
        public PassportClassifier createFromParcel(Parcel parcel) {
            return new PassportClassifier();
        }

        @Override
        public PassportClassifier[] newArray(int size) {
            return new PassportClassifier[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
