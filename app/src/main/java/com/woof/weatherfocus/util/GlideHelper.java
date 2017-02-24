package com.woof.weatherfocus.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Woof on 2/24/2017.
 */

public class GlideHelper {
    /**
     * 该类主要用于图片的加载以及清除，优化内存的占用
     */
    public static void imageLoad(Context context, @DrawableRes int imageRes, ImageView view){
        Glide.with(context).load(imageRes).into(view);
    }

    public static void imageClear(Context context){
        Glide.get(context).clearMemory();
    }
}
