package com.sample.blinkid;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;

import timber.log.Timber;

public class BlinkApplication extends Application {

    public static final String ACTION_IMAGE_DETECTED = "com.sample.blinkid.action.IMAGE_DETECTED";

    private final SparseArrayCompat<Bitmap> bitmapArray = new SparseArrayCompat<>();

    @Nullable
    private static BlinkApplication application;

    public BlinkApplication() {
        application = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }

    @NonNull
    public static BlinkApplication getApplication() {
        if (application == null) {
            throw new IllegalStateException(
                    "trying to get the application before the application started???");
        }
        return application;
    }

    @Override
    public void onTrimMemory(int level) {
        if (level <= TRIM_MEMORY_UI_HIDDEN) {
            for (int i = 0, l = bitmapArray.size(); i < l; i++) {
                Bitmap bitmap = bitmapArray.get(bitmapArray.keyAt(i));
                bitmap.recycle();
            }
            bitmapArray.clear();
        } else {
            super.onTrimMemory(level);
        }
    }

    public void setDetectedImage(int id, Bitmap image) {
        Bitmap detectedImage = bitmapArray.get(id);
        if (detectedImage != null && !detectedImage.isRecycled()) {
            detectedImage.recycle();
        }
        bitmapArray.put(id, image);
    }

    @Nullable
    public Bitmap getDetectedImage(int id) {
        return bitmapArray.get(id);
    }
}
