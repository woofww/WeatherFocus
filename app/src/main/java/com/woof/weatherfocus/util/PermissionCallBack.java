package com.woof.weatherfocus.util;

/**
 * Created by Woof on 3/2/2017.
 */

public interface PermissionCallBack {
    /**
     * 该接口用于动态权限回调
     */
    void permissionGranted();
    void permissionRefused();
}
