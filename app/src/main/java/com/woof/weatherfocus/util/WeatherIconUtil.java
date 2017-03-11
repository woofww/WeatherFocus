package com.woof.weatherfocus.util;

import android.util.Log;

import com.woof.weatherfocus.R;

/**
 * Created by Woof on 3/9/2017.
 */

public class WeatherIconUtil {

    public static void initIcon() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferenceUtil.getInstance().setInt("未知", R.mipmap.none);
                SharedPreferenceUtil.getInstance().setInt("晴", R.mipmap.sunny);
                SharedPreferenceUtil.getInstance().setInt("多云", R.mipmap.clouds);
                SharedPreferenceUtil.getInstance().setInt("少云", R.mipmap.few_clouds);
                SharedPreferenceUtil.getInstance().setInt("晴间多云", R.mipmap.sunny_clouds);
                SharedPreferenceUtil.getInstance().setInt("阴", R.mipmap.haze);
                SharedPreferenceUtil.getInstance().setInt("雷阵雨", R.mipmap.weather_thunder);
                SharedPreferenceUtil.getInstance().setInt("毛毛雨/细雨", R.mipmap.rain_day);
                SharedPreferenceUtil.getInstance().setInt("阵雨", R.mipmap.weather_thunder);
                SharedPreferenceUtil.getInstance().setInt("小雨", R.mipmap.rain_day);
                SharedPreferenceUtil.getInstance().setInt("中雨", R.mipmap.weather_showers_day);
                SharedPreferenceUtil.getInstance().setInt("大雨", R.mipmap.weather_showers_day);
                SharedPreferenceUtil.getInstance().setInt("雨夹雪", R.mipmap.snow_rain);
                SharedPreferenceUtil.getInstance().setInt("小雪", R.mipmap.weather_snow);
                SharedPreferenceUtil.getInstance().setInt("中雪", R.mipmap.snow_scattered_day);
                SharedPreferenceUtil.getInstance().setInt("大雪", R.mipmap.heavy_snow);
                SharedPreferenceUtil.getInstance().setInt("雾", R.mipmap.fog);
                SharedPreferenceUtil.getInstance().setInt("霾", R.mipmap.haze);
            }
        }).start();

    }
}
