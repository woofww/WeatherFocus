package com.woof.weatherfocus.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;


/**
 * Created by Woof on 2/23/2017.
 */

public class StatusBarUtil {
    /**
     * /**
     * 该部分实现StatusBar和ToolBar的沉浸式，解决了imageview无法扩展到statusbar的问题
     * @param toolbar
     * @param context
     */
    public static void setImmersiveStatusBarToolbar(@NonNull Toolbar toolbar, Context context) {
        ViewGroup.MarginLayoutParams toolLayoutParams = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
        toolLayoutParams.height = EnvirUtil.getStatusBarHeight(context) + EnvirUtil.getActionBarSize(context);
        toolbar.setLayoutParams(toolLayoutParams);
        toolbar.setPadding(0, EnvirUtil.getStatusBarHeight(context), 0, 0);
        toolbar.requestLayout();
    }
}
