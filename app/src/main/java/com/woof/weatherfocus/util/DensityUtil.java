package com.woof.weatherfocus.util;

import android.content.res.Resources;

/**
 * Created by Woof on 2/28/2017.
 */

public class DensityUtil {
    private static Resources sRes = Resources.getSystem();
    private static int sDensityDpi = sRes.getDisplayMetrics().densityDpi;
    private static float sScaledDensity = sRes.getDisplayMetrics().scaledDensity;

    public static int dp2px(float value) {
        final float scale = sDensityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

}
