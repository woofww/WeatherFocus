package com.woof.weatherfocus.util;

import android.os.Build;

/**
 * Created by Woof on 3/11/2017.
 */

public class SDKUtil {

    public static boolean isMarshmallow() {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
