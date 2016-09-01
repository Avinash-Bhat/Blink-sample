package com.sample.blinkid;

import com.microblink.image.Image;
import com.microblink.image.ImageListener;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

class BroadcastingImageListener implements ImageListener {

    private final BlinkApplication application = BlinkApplication.getApplication();

    BroadcastingImageListener() {
    }

    @Override
    public void onImageAvailable(Image image) {
        application.setDetectedImage(image.convertToBitmap());
        Intent intent = new Intent(BlinkApplication.ACTION_IMAGE_DETECTED);
        LocalBroadcastManager.getInstance(application).sendBroadcast(intent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
    }

    public static final Parcelable.Creator<BroadcastingImageListener> CREATOR
            = new Creator<BroadcastingImageListener>() {
        @Override
        public BroadcastingImageListener createFromParcel(Parcel parcel) {
            return new BroadcastingImageListener();
        }

        @Override
        public BroadcastingImageListener[] newArray(int size) {
            return new BroadcastingImageListener[size];
        }
    };
}
