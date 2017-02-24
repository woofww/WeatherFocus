package com.woof.weatherfocus.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Woof on 2/24/2017.
 */

public class NetworkUtil {

    public static boolean isNetworkConnected(Context mContext){
        if (mContext != null){
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null){
                // 获取网络信息，判断是否是否有效
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
