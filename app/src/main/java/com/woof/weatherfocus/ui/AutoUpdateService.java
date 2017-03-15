package com.woof.weatherfocus.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.woof.weatherfocus.util.SharedPreferenceUtil;

/**
 * Created by Woof on 3/15/2017.
 */

public class AutoUpdateService extends Service{

    private SharedPreferenceUtil mSharedPreferenceUtil;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        // 当服务启动时候，onCreate只会启动一次
        super.onCreate();
        mSharedPreferenceUtil = SharedPreferenceUtil.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this) {
            if (mSharedPreferenceUtil.getAutoUpdate() != 0) {

            }
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
}
