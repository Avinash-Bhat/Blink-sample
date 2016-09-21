package com.sample.blinkid;

import com.microblink.image.Image;
import com.microblink.image.ImageListener;
import com.microblink.image.ImageType;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import static com.microblink.image.ImageType.DEWARPED;
import static com.microblink.image.ImageType.SUCCESSFUL_SCAN;

class BroadcastingImageListener implements ImageListener {

    private final BlinkApplication application = BlinkApplication.getApplication();

    private final boolean onlyDewraped;

    BroadcastingImageListener(boolean onlyDewraped) {
        this.onlyDewraped = onlyDewraped;
    }

    private BroadcastingImageListener(Parcel parcel) {
        onlyDewraped = (parcel.readInt() == 1);
    }

    @Override
    public void onImageAvailable(Image image) {
        ImageType imageType = onlyDewraped ? DEWARPED : SUCCESSFUL_SCAN;
        if (image.getImageType() == imageType) {
            sendBroadcast(image.clone());
        }
    }

    private void sendBroadcast(Image image) {
        //todo do this on bg thread
        try {
            application.setDetectedImage(image.convertToBitmap());
            Intent intent = new Intent(BlinkApplication.ACTION_IMAGE_DETECTED);
            LocalBroadcastManager.getInstance(application).sendBroadcast(intent);
        } finally {
            image.dispose();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(onlyDewraped ? 1 : 0);
    }

    public static final Parcelable.Creator<BroadcastingImageListener> CREATOR
            = new Creator<BroadcastingImageListener>() {
        @Override
        public BroadcastingImageListener createFromParcel(Parcel parcel) {
            return new BroadcastingImageListener(parcel);
        }

        @Override
        public BroadcastingImageListener[] newArray(int size) {
            return new BroadcastingImageListener[size];
        }
    };
}
