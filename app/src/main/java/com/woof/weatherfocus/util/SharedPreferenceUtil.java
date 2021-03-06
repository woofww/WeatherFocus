package com.woof.weatherfocus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.woof.weatherfocus.base.BaseApplication;

/**
 * SharedPreference工具类用于存放
 * SharedPreference在set操作的时候需要apply()才能生效
 * Created by Woof on 3/2/2017.
 */

public class SharedPreferenceUtil {

    public static final String CITY_NAME = "city"; //用于存储当前城市或者定位城市
    public static final String CACHE_CLEAR = "clear_cache"; //操作缓存状态
    public static final String AUTO_UPDATE = "update_time_change"; //自动更新时长
    public static final String NOLIFICATION_STATUS = "nolification_status"; //存储通知状态
    public static final String TEMP_UNIT = "temp_unit";
    public static final String ANIMATION_SWITCH = "animation_switch"; //RecyclerView的动画切换

    public static final int ONE_HOUR = 1000 * 60 * 60; // ms为基本单位

    private SharedPreferences mSharedPreferences;

    // 阻止外部对象直接进行实例化，维护封装性
    private SharedPreferenceUtil(){
        mSharedPreferences = BaseApplication.getAppContext().
                getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    // 通过类内部构建实例并传递实例
    private static class SharedPreferenceHolder {
        private static final SharedPreferenceUtil sInstance = new SharedPreferenceUtil();
    }

    // 获取工具类实例
    public static SharedPreferenceUtil getInstance() {
        return SharedPreferenceHolder.sInstance;
    }

    // 自动更新时间 hours
    public void setAutoUpdate(int t) {
        mSharedPreferences.edit().putInt(AUTO_UPDATE, t).apply();
    }

    // 获取自动更新时间
    public int getAutoUpdate() {
        return mSharedPreferences.getInt(AUTO_UPDATE, 3);
    }

    // 通过SP进行主页面的城市存储
    public void setCity(String name) {
        mSharedPreferences.edit().putString(CITY_NAME, name).apply();
    }

    public String getCity() {
        return mSharedPreferences.getString(CITY_NAME, "北京");
    }

    // 设置Recyclerview的动态效果的开闭
    public void setAnimationSwitch(boolean a) {
        mSharedPreferences.edit().putBoolean(ANIMATION_SWITCH, a).apply();
    }

    public boolean getAnimationSwitch() {
        return mSharedPreferences.getBoolean(ANIMATION_SWITCH, false);
    }

    // 对温度单位进行设置
    public void setTempUnit(boolean b) {
        mSharedPreferences.edit().putBoolean(TEMP_UNIT, b).apply();
    }

    public boolean getTempUnit() {
        return mSharedPreferences.getBoolean(TEMP_UNIT, false);
    }


    public void setInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int initValue) {
        return mSharedPreferences.getInt(key, initValue);
    }

}
