package com.sample.blinkid;

import com.microblink.detectors.DecodingInfo;
import com.microblink.geometry.Rectangle;
import com.microblink.recognizers.blinkocr.BlinkOCRRecognizerSettings;
import com.microblink.recognizers.blinkocr.engine.BlinkOCREngineOptions;
import com.microblink.recognizers.blinkocr.parser.regex.RegexParserSettings;
import com.microblink.recognizers.templating.TemplatingRecognizerSettings;
import com.microblink.results.ocr.OcrFont;

import android.support.annotation.NonNull;

class Recognizers {

    private static final String ID_NAME = "Name";

    private static Recognizers sInstance = new Recognizers();

    static Recognizers getInstance() {
        return sInstance;
    }

    private Recognizers() {
    }

    BlinkOCRRecognizerSettings sixdeeId() {
        BlinkOCRRecognizerSettings settings = new BlinkOCRRecognizerSettings();
        settings.setParserDecodingInfos(new DecodingInfo[]{
                setupName(settings)
        }, ID_NAME);
        return settings;
    }

    @NonNull
    private DecodingInfo setupName(@NonNull TemplatingRecognizerSettings settings) {
        RegexParserSettings nameParser = new RegexParserSettings("[\\w\\s\\.\\-]+");
        BlinkOCREngineOptions options = nameParser.getOcrEngineOptions();
        options.addAllDigitsToWhitelist(OcrFont.OCR_FONT_ANY);
        options.setColorDropoutEnabled(false);
        options.setMinimumCharHeight(10);
        settings.addParserToParserGroup(ID_NAME, ID_NAME, nameParser);

        // 6d: the ID is of 84mm height and 54mm width
        // the name is at top: 53mm, bottom: 58mm, left: 4, right: 52mm
         return new DecodingInfo(getRectangle(3, 52, 53, 58, 54, 84), 100, ID_NAME);

        // DL: the ID is of 54mm height and 89mm width
        // the name is at top: 18mm, bottom: 20.5mm, left: 39.5mm, right: 85mm
//        return new DecodingInfo(getRectangle(39.5f, 85, 54, 58, 89f, 54f), 200, ID_NAME);
    }

    @NonNull
    private Rectangle getRectangle(float left, float right, float top, float bottom, float width,
            float height) {
        return new Rectangle(left / width, top / height, (right - left) / width,
                (bottom - top) / height);
    }
}
