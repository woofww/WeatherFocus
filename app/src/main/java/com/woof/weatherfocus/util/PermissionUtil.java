package com.woof.weatherfocus.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Woof on 3/2/2017.
 */

public class PermissionUtil {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static boolean hasPermission(Context context, String... permissions) {

        if (SDKUtil.isMarshmallow()) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
            return true;
        } else {
            return true;
        }
    }

    public static void requestPermission(int code, Activity activity, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, code);
    }


}
