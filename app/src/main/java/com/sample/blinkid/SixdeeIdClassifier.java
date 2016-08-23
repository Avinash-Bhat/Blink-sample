package com.sample.blinkid;

import com.microblink.recognizers.blinkocr.BlinkOCRRecognitionResult;
import com.microblink.recognizers.blinkocr.DocumentClassifier;

import android.os.Parcel;
import android.text.TextUtils;

class SixdeeIdClassifier implements DocumentClassifier {

    static final String ID_NAME = "Name";

    SixdeeIdClassifier() {
    }

    private SixdeeIdClassifier(Parcel in) {
    }

    @Override
    public String classifyDocument(BlinkOCRRecognitionResult result) {
        String parsedId = result.getParsedResult(ID_NAME);
        if (!TextUtils.isEmpty(parsedId)) {
            return ID_NAME;
        }
        return null;
    }

    public static final Creator<SixdeeIdClassifier> CREATOR = new Creator<SixdeeIdClassifier>() {
        @Override
        public SixdeeIdClassifier createFromParcel(Parcel parcel) {
            return new SixdeeIdClassifier(parcel);
        }

        @Override
        public SixdeeIdClassifier[] newArray(int size) {
            return new SixdeeIdClassifier[size];
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
