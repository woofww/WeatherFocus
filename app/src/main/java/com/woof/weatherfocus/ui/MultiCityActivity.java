package com.woof.weatherfocus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.woof.weatherfocus.R;
import com.woof.weatherfocus.base.ToolBarActivity;
import com.woof.weatherfocus.model.db.DBHelper;
import com.woof.weatherfocus.model.entity.City;
import com.woof.weatherfocus.model.entity.Province;
import com.woof.weatherfocus.model.entity.Zone;
import com.woof.weatherfocus.ui.adapter.CityAdapter;
import com.woof.weatherfocus.util.SharedPreferenceUtil;
import com.woof.weatherfocus.util.listener.OnRecyclerViewItemListerner;
import com.woof.weatherfocus.util.CityUtil;
import com.woof.weatherfocus.util.TextUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class MultiCityActivity extends ToolBarActivity {

    private RecyclerView mRecyclerView;
    // 用于存储显示数据
    private ArrayList<String> dataList = new ArrayList<>();
    private Province mProvince;
    private City mCity;
    private List<Province> mProvinceList = new ArrayList<>();
    private List<City> mCityList = new ArrayList<>();
    private List<Zone> mZoneList = new ArrayList<>();
    private CityAdapter mCityAdapter;

    // RecyclerView的多级显示层级标注, 待更新区县级城市, 定位功能正常之后实现
    static final int PROVINCE_LEVEL = 0;
    static final int CITY_LEVEL = 1;
    static final int ZONE_LEVEL = 2;
    private int currentLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView = ((RecyclerView) findViewById(R.id.choose_city_rv));
        new Thread(new Runnable() {
            @Override
            public void run() {
                initDB();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerView();
                        queryProvince();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == PROVINCE_LEVEL){
            this.finish();
        } else {
            queryProvince();
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBHelper.getInstance().dataBaseClose();
    }

    /**
     * 当canBack返回true的时候，建立后退按钮
     * @return
     */
    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.choose_city;
    }

    private void initRecyclerView() {
        // RecyclerView的初始化步骤
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mCityAdapter = new CityAdapter(this, dataList);
        mRecyclerView.setAdapter(mCityAdapter);
        // 设置点击监听回调
        mCityAdapter.setOnRecyclerViewItemListerner(new OnRecyclerViewItemListerner() {
            @Override
            public void onItemClick(View view, int position) {
                if (currentLevel == PROVINCE_LEVEL){
                    mProvince = mProvinceList.get(position);
                    mRecyclerView.smoothScrollToPosition(0);
                    queryCity();
                } else if (currentLevel == CITY_LEVEL){
                    // 该部分用于修改主布局的城市
//                    String city = TextUtil.replaceCityName(mCityList.get(position).CityName);
//                    SharedPreferenceUtil.getInstance().setCity(city);
//                    startActivity(new Intent(MultiCityActivity.this, MainActivity.class));
//                    Log.e("点击", city);
                    mCity = mCityList.get(position);
                    mRecyclerView.smoothScrollToPosition(0);
                    queryZone();
                } else if (currentLevel == ZONE_LEVEL){
                    String zone;
                    if (mZoneList.size() == 0) {
                        zone = TextUtil.replaceCityName(dataList.get(0));
                    } else {
                        zone = TextUtil.replaceCityName(mZoneList.get(position).ZoneName);
                    }
                    SharedPreferenceUtil.getInstance().setCity(zone);
                    startActivity(new Intent(MultiCityActivity.this, MainActivity.class));
                }
            }
        });
    }

    private void queryProvince() {

        getToolbar().setTitle("选择省份");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mProvinceList.isEmpty()){
                    mProvinceList.addAll(CityUtil.loadProvince(DBHelper.getInstance().getDataBase()));
                }
                dataList.clear();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 遍历ProvinceList导入datalist
                        for (Province province : mProvinceList){
                            dataList.add(province.ProName);
                        }
                        // 获取省级列表
                        mCityAdapter.notifyDataSetChanged();
                        mRecyclerView.smoothScrollToPosition(0);
                        currentLevel = PROVINCE_LEVEL;
                    }
                });
            }
        }).start();
    }

    /**
     * 此处不需要进行判断，判断之后市级数据内容将无法进行改变
     * if (mCityList.isEmpty()){
     *      mCityList.addAll(CityUtil.loadCity(DBHelper.getInstance().getDataBase(), mProvince.ProSort));
     * }
     */
    private void queryCity() {
        // 查询城市的时候不需要进行
        getToolbar().setTitle("选择城市");
        dataList.clear();
        mCityAdapter.notifyDataSetChanged();

        // 此处通过选中的mProvince来进行判断ProSort，直接进行赋值
        mCityList = CityUtil.loadCity(DBHelper.getInstance().getDataBase(), mProvince.ProSort);
        for (City city : mCityList){
            dataList.add(city.CityName);
        }
        mCityAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
        currentLevel = CITY_LEVEL;

    }

    private void queryZone() {
        mZoneList = CityUtil.loadZone(DBHelper.getInstance().getDataBase(), mCity.CitySort);
        if (mZoneList.size() == 0){
            getToolbar().setTitle("选择城市");
            dataList.clear();
            dataList.add(mCity.CityName);
        } else {
            getToolbar().setTitle("选择县级市");
            dataList.clear();
            mCityAdapter.notifyDataSetChanged();

            for (Zone zone : mZoneList){
                dataList.add(zone.ZoneName);
            }
        }

        mCityAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
        currentLevel = ZONE_LEVEL;

    }

    private void initDB() {
        try {
            DBHelper.getInstance().openDataBase();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
