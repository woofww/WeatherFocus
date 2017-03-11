package com.woof.weatherfocus.util;

/**
 * Created by Woof on 3/9/2017.
 * 完成双击back键推出功能，检测与之1500ms
 */

public class DoubleClickUtil {

    private static long mLastClick = 0L;

    private static final int THRESHOLD = 1500; // 暂定1500ms

    /**
     * CC means checkclick
     */
    public static boolean CC() {

        long current = System.currentTimeMillis();
        boolean state = (current - mLastClick) < THRESHOLD; //如果小于阈值
        mLastClick = current;
        return state;
    }
}
