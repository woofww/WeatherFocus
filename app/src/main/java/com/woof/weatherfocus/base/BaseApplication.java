package com.woof.weatherfocus.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

/**
 * Created by Woof on 2/19/2017.
 * This base class use to get the application context
 */

public class BaseApplication extends Application{

    private static BaseApplication App;
    private static String sCacheDir;
    private static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * Configure context instance
         */
        App = this;
        sAppContext = getApplicationContext();


    }

    /**
     * Used to return Application Instance
     * @return
     */
    public static BaseApplication getApplication(){
        return App;
    }

    public static Context getAppContext(){
        return sAppContext;
    }

    public static String getAppCacheDir(){return sCacheDir;}

    /**
     * if SDCard exist. write the cache into SDCard, if not write into memory
     */

    public static void judgeCacheLocation(Context mContext){
        if (mContext.getExternalCacheDir() != null && existSDCard()){
            sCacheDir = mContext.getExternalCacheDir().toString();
        } else {
            sCacheDir = mContext.getCacheDir().toString();
        }
    }

    private static boolean existSDCard() {
        return android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
