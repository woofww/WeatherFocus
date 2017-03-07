package com.woof.weatherfocus.util;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.woof.weatherfocus.R;
import com.woof.weatherfocus.base.BaseApplication;

import butterknife.BindView;

/**
 * Created by Woof on 3/5/2017.
 */

public class LocationUtil implements AMapLocationListener{
    @BindView(R.id.refresh_swipe)
    SwipeRefreshLayout mRefreshLayout;

    public AMapLocationClient mAMapLocationClient = null;
    public AMapLocationClientOption mAMapLocationClientOption = null;

    /**
     * 高德定位
     */
    private void location() {
        mRefreshLayout.setRefreshing(true);
        //初始化定位
        mAMapLocationClient = new AMapLocationClient(BaseApplication.getAppContext());
        //设置定位回调监听
        mAMapLocationClient.setLocationListener(this);
        mAMapLocationClientOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）ad
        mAMapLocationClientOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mAMapLocationClientOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mAMapLocationClientOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mAMapLocationClientOption.setMockEnable(false);
        //设置定位间隔 单位毫秒
        int tempTime = SharedPreferenceUtil.getInstance().getAutoUpdate();
        if (tempTime == 0) {
            tempTime = 100;
        }
        mAMapLocationClientOption.setInterval(tempTime * SharedPreferenceUtil.ONE_HOUR);
        //给定位客户端对象设置定位参数
        mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
        //启动定位
        mAMapLocationClient.startLocation();
    }

    /**
     * 定位改变的时候，修改SP配置文件的对应的city参数
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                aMapLocation.getLocationType();
                SharedPreferenceUtil
                        .getInstance()
                        .setCity(TextUtil.replaceCityName(aMapLocation.getCity()));
            }
        }
    }
}
