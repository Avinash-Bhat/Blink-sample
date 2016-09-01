package com.sample.blinkid;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BlinkApplication extends Application {

    public static final String ACTION_IMAGE_DETECTED = "com.sample.blinkid.action.IMAGE_DETECTED";
    @Nullable
    private static BlinkApplication application;

    private Bitmap detectedImage;

    public BlinkApplication() {
        application = this;
    }

    @NonNull
    public static BlinkApplication getApplication() {
        if (application == null) {
            throw new IllegalStateException(
                    "trying to get the application before the application started???");
        }
        return application;
    }

    public void setDetectedImage(Bitmap image) {
        if (detectedImage != null && !detectedImage.isRecycled()) {
            detectedImage.recycle();
        }
        detectedImage = image;
    }

    @Nullable
    public Bitmap getDetectedImage() {
        return detectedImage;
    }
}
