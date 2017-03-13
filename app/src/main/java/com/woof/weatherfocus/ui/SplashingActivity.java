package com.woof.weatherfocus.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by Woof on 3/13/2017.
 */

public class SplashingActivity extends Activity {

    private static final  String TAG = SplashingActivity.class.getSimpleName();
    private SwitchHandler mHander = new SwitchHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        mHander.sendEmptyMessageAtTime(1, 1000);
    }

    private class SwitchHandler extends Handler {
        private WeakReference<SplashingActivity> mWeakReference;
        public SwitchHandler(SplashingActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashingActivity activity = mWeakReference.get();
            if (activity != null) {
                SplashingActivity.this.startActivity(new Intent(SplashingActivity.this, MainActivity.class));
                activity.finish();
            }
        }
    }
}
