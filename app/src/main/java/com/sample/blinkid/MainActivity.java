package com.sample.blinkid;

import com.sample.blinkid.databinding.ActivityMainBinding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private MainViewModel model;

    static {
        Timber.plant(new Timber.DebugTree());
    }

    @Nullable
    private ImageBroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.microblink.util.Log.setLogLevel(com.microblink.util.Log.LogLevel.LOG_VERBOSE);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        model = new MainViewModel(this);
        binding.setScanner(model);
        receiver = new ImageBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter(BlinkApplication.ACTION_IMAGE_DETECTED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!model.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        setDetectedImage();
    }

    private class ImageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            setDetectedImage();
        }
    }

    private void setDetectedImage() {
        Bitmap bitmap = BlinkApplication.getApplication().getDetectedImage();
        model.detectedImage.set(new BitmapDrawable(getResources(), bitmap));
    }
}

