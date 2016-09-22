package com.sample.blinkid;

import com.microblink.recognizers.blinkocr.BlinkOCRRecognitionResult;
import com.microblink.recognizers.blinkocr.DocumentClassifier;

import android.os.Parcel;
import android.text.TextUtils;

class NationalIdClassifier implements DocumentClassifier {

    static final String ID_NAME = "Name";

    NationalIdClassifier() {
    }


    @Override
    public String classifyDocument(BlinkOCRRecognitionResult result) {
        String parsedId = result.getParsedResult(ID_NAME, ID_NAME);
        if (!TextUtils.isEmpty(parsedId)) {
            return ID_NAME;
        }
        return null;
    }

    public static final Creator<NationalIdClassifier> CREATOR
            = new Creator<NationalIdClassifier>() {
        @Override
        public NationalIdClassifier createFromParcel(Parcel parcel) {
            return new NationalIdClassifier();
        }

        @Override
        public NationalIdClassifier[] newArray(int size) {
            return new NationalIdClassifier[size];
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
