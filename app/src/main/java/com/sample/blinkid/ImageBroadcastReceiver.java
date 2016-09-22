package com.sample.blinkid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

class ImageBroadcastReceiver extends BroadcastReceiver {

    private Scanner scanner;

    public ImageBroadcastReceiver(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        scanner.onImageDetected();
    }
}