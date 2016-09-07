package com.sample.blinkid;

import com.microblink.detectors.DecodingInfo;
import com.microblink.detectors.document.DocumentDetectorSettings;
import com.microblink.detectors.document.DocumentSpecification;
import com.microblink.detectors.document.DocumentSpecificationPreset;
import com.microblink.geometry.Rectangle;
import com.microblink.recognizers.blinkocr.BlinkOCRRecognizerSettings;
import com.microblink.recognizers.blinkocr.engine.BlinkOCREngineOptions;
import com.microblink.recognizers.blinkocr.parser.regex.RegexParserSettings;
import com.microblink.recognizers.templating.TemplatingRecognizerSettings;
import com.microblink.results.ocr.OcrFont;

import android.support.annotation.NonNull;

import static com.sample.blinkid.SixdeeIdClassifier.ID_NAME;

class Recognizers {

    private static Recognizers sInstance = new Recognizers();

    static Recognizers getInstance() {
        return sInstance;
    }

    private Recognizers() {
    }

    BlinkOCRRecognizerSettings sixdeeId(boolean needDewrapped) {
        BlinkOCRRecognizerSettings settings = new BlinkOCRRecognizerSettings();
        DecodingInfo[] decodingInfos;
        if (needDewrapped) {
            decodingInfos = new DecodingInfo[]{
                    setupName(settings)
                    , new DecodingInfo(new Rectangle(0, 0, 1, 1), 350, "noParsers")
            };
        } else {
            decodingInfos = new DecodingInfo[]{
                    setupName(settings)
            };
        }
        DocumentSpecification idSpec = DocumentSpecification.createFromPreset(
                DocumentSpecificationPreset.DOCUMENT_SPECIFICATION_PRESET_ID1_CARD);
        idSpec.setAspectRatio(1.733);
        idSpec.setDecodingInfos(decodingInfos);
        DocumentDetectorSettings detectorSettings = new DocumentDetectorSettings(
                new DocumentSpecification[]{idSpec});
        detectorSettings.setNumStableDetectionsThreshold(3);
        settings.setDetectorSettings(detectorSettings);
        settings.setDocumentClassifier(new SixdeeIdClassifier());
        settings.setParserDecodingInfos(decodingInfos, ID_NAME);
        return settings;
    }

    @NonNull
    private DecodingInfo setupName(@NonNull TemplatingRecognizerSettings settings) {
        RegexParserSettings nameParser = new RegexParserSettings("[A-Za-z ]+");
        BlinkOCREngineOptions options = nameParser.getOcrEngineOptions();
        options.addLowercaseCharsToWhitelist(OcrFont.OCR_FONT_ANY);
        options.addUppercaseCharsToWhitelist(OcrFont.OCR_FONT_ANY);
        options.setColorDropoutEnabled(false);
        options.setMinimumCharHeight(10);
        settings.addParserToParserGroup(ID_NAME, ID_NAME, nameParser);

        Rectangle rectangle;
        // 6d: the ID is of 84mm height and 54mm width
        // the name is at top: 53mm, bottom: 58mm, left: 4, right: 52mm
//        rectangle = getRectangle(3, 52, 53, 58, 54, 84);

        // DL: the ID is of 54mm height and 89mm width
        // the name is at top: 18mm, bottom: 20.5mm, left: 39.5mm, right: 85mm
//        rectangle = getRectangle(39.5f, 85, 54, 58, 89f, 54f);

        // visiting ID: the ID is of 41mm height and 89mm width
        // the name is at top: 3mm, bottom: 7mm, left: 31mm, right: 75mm
        rectangle = getRectangle(7, 75, 3, 7, 89, 41);

        return new DecodingInfo(rectangle, getDewrapedHeight(rectangle.getHeight()), ID_NAME);
    }

    private int getDewrapedHeight(float relHeight) {
        return (int) Math.ceil(relHeight * 1000);
    }

    @NonNull
    private Rectangle getRectangle(float left, float right, float top, float bottom, float width,
            float height) {
        return new Rectangle(left / width, top / height, (right - left) / width,
                (bottom - top) / height);
    }
}
