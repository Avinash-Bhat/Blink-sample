package com.sample.blinkid;

import com.microblink.detectors.DecodingInfo;
import com.microblink.detectors.document.DocumentDetectorSettings;
import com.microblink.detectors.document.DocumentSpecification;
import com.microblink.detectors.document.DocumentSpecificationPreset;
import com.microblink.geometry.Rectangle;
import com.microblink.recognizers.blinkocr.BlinkOCRRecognitionResult;
import com.microblink.recognizers.blinkocr.BlinkOCRRecognizerSettings;
import com.microblink.recognizers.blinkocr.engine.BlinkOCREngineOptions;
import com.microblink.recognizers.blinkocr.parser.regex.RegexParserSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.recognizers.templating.TemplatingRecognizerSettings;
import com.microblink.results.ocr.OcrFont;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import timber.log.Timber;

import static com.sample.blinkid.PassportClassifier.ID_NAME;

public class PassportScanner extends Scanner {

    public final ObservableField<String> fullName = new ObservableField<>();

    PassportScanner(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    protected RecognizerSettings[] getRecognizerSettings() {
        BlinkOCRRecognizerSettings settings = new BlinkOCRRecognizerSettings();
        DecodingInfo[] decodingInfos;
        if (detectDewrappedOnly.get()) {
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
        settings.setDocumentClassifier(new PassportClassifier());
        settings.setParserDecodingInfos(decodingInfos, ID_NAME);
        return new RecognizerSettings[]{
                settings
        };
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

        // the passport is of 81mm height and 118mm width
        // the name is at top: 16mm, bottom: 19mm, left: 36mm, right: 109mm
        Rectangle rectangle = getRectangle(36, 109, 16, 19, 118, 81);

        return new DecodingInfo(rectangle, getDewrapedHeight(rectangle.getHeight()), ID_NAME);
    }


    @Override
    protected void onResult(BlinkOCRRecognitionResult result) {
        String name = result
                .getParsedResult(NationalIdClassifier.ID_NAME, NationalIdClassifier.ID_NAME);
        Timber.v("passport name: %s", name);
        fullName.set(name);
    }
}
