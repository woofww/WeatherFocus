package com.woof.weatherfocus.util;

import android.content.Context;
import android.util.TypedValue;

import com.woof.weatherfocus.base.BaseApplication;

/**
 * Created by Woof on 2/23/2017.
 */

public class EnvirUtil {
    private static int sStatusBarHeight;

    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return DensityUtil.dp2px(44);
    }

    public static int getStatusBarHeight(Context mContext) {
        if (sStatusBarHeight == 0) {
            int resourceId =
                    mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sStatusBarHeight = mContext.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return sStatusBarHeight;
    }
}
