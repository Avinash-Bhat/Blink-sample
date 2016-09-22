package com.sample.blinkid;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

public abstract class ScanningFragment<S extends Scanner> extends Fragment {

    private ImageBroadcastReceiver receiver;

    protected S scanner;

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(BlinkApplication.ACTION_IMAGE_DETECTED);
        receiver = new ImageBroadcastReceiver(scanner);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
        // if the image is there, try to set the image
        if (scanner != null) {
            scanner.onImageDetected();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!scanner.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStop() {
        if (receiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        }
        super.onStop();
    }
}
