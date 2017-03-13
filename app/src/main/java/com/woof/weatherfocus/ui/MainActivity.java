package com.woof.weatherfocus.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.woof.weatherfocus.R;
import com.woof.weatherfocus.base.BaseActivity;
import com.woof.weatherfocus.base.BaseApplication;
import com.woof.weatherfocus.model.Constant;
import com.woof.weatherfocus.model.entity.Weather;
import com.woof.weatherfocus.model.entity.WeatherEntity;
import com.woof.weatherfocus.network.RetrofitHelper;
import com.woof.weatherfocus.ui.adapter.MainPageAdapter;
import com.woof.weatherfocus.util.DoubleClickUtil;
import com.woof.weatherfocus.util.NetworkUtil;
import com.woof.weatherfocus.util.PermissionUtil;
import com.woof.weatherfocus.util.SharedPreferenceUtil;
import com.woof.weatherfocus.util.StatusBarUtil;
import com.woof.weatherfocus.util.TextUtil;
import com.woof.weatherfocus.util.TimeUtil;
import com.woof.weatherfocus.util.WeatherIconUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static java.util.Calendar.HOUR_OF_DAY;


/**
 * MainActivity主要完成了Controller的功能，后续考虑将业务逻辑分离，用MVP模式进行优化
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, AMapLocationListener {
    // 注意Complier包的导入，否则报空指针，强烈注意
    /* 完成主界面部分的控件初始化 */
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.col_toolbar)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.current_temp)
    TextView currentTemp;
    @BindView(R.id.error_iv)
    ImageView networkErrorImage;

    /* 主界面RecyclerView部分的初始化 */
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_swipe)
    SwipeRefreshLayout mRefreshLayout;

    // 对定位Client进行声明
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;


    // 用于对时间段进行判定
    Calendar calendar = Calendar.getInstance();

    // 权限请求码
    int requestCode = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        StatusBarUtil.setImmersiveStatusBarToolbar(mToolbar, this);
        WeatherIconUtil.initIcon();
        initDrawer();
        initFab();
        initBanner();
        initView();
        getWeatherByNetwork();
    }

    private void initView() {
        initRefreshLayout();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 通过对initData()网络请求线程延时, 等待高德的定位回调对默认城市进行更新，此处需要注意
                Snackbar.make(view, "更新定位...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                locationPermission();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                    }
                }, 1500);
            }
        });
    }

    private void initData() {
        if (!NetworkUtil.isNetworkConnected(MainActivity.this)) {
//            SharedPreferenceUtil.getInstance().setCity("北京");
//            Log.e("恢复默认", "完成");
            networkErrorImage.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            setToolbarTitle("查询失败");
            Toast.makeText(MainActivity.this, "请检查您的网络设置", LENGTH_SHORT).show();
        } else {
            networkErrorImage.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            setToolbarTitle(SharedPreferenceUtil.getInstance().getCity());
            getWeatherByNetwork();
        }
        // 可以在刷新的时候对banner进行更新
        initBanner();
        Log.e("响应点击", SharedPreferenceUtil.getInstance().getCity());
    }

    private void initDrawer() {
        if (mNavView != null){
            mNavView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    private void initBanner() {
        int i = TimeUtil.getSystemCurrentHour(calendar.get(HOUR_OF_DAY));
        switch (i) {
            case 1:
                banner.setImageDrawable(this.getResources().getDrawable(R.mipmap.sunrise));
                break;
            case 2:
                banner.setImageDrawable(this.getResources().getDrawable(R.mipmap.sunset));
                break;
            case 3:
                banner.setImageDrawable(this.getResources().getDrawable(R.mipmap.night));
                break;
            default:
                break;
        }
    }

    private void initRefreshLayout() {

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setToolbarTitle(String title) {
        /**
         * 实现了在城市更改的时候，动态对title进行改动
         * @param title
         */
        mToolbarLayout.setTitleEnabled(true);
        mToolbarLayout.setTitle(title);
    }

    private void getWeatherByNetwork() {

        /**
         * 主界面显示城市被存储在SP当中
         */
        String cityName = SharedPreferenceUtil.getInstance().getCity();
        getWeatherData(cityName, this);
    }

    private void getWeatherData(String city, final Context context) {

        Call<Weather> call = RetrofitHelper
                .getInstance()
                .getWeatherService()
                .getWeather(city, Constant.Key);
        if (!NetworkUtil.isNetworkConnected(context)){
            // 此处需要在布局中添加network error的响应图片
            Toast.makeText(context, "当前没有网络，请查看您的网络连接...", Toast.LENGTH_LONG).show();
            return;
        }
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                // 这里需要进行无网判断
                String status = response.body().mWeather.get(0).status;
                Log.e("调用异步请求", status);
                switch (status) {
                    case "no more request":
                        Toast.makeText(context, "API免费次数以用完", Toast.LENGTH_LONG).show();
                        break;
                    case "unknown city":
                        Toast.makeText(context, "抱歉，查询不到该城市", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Weather weather = response.body();
                        WeatherEntity mWeatherEntity = weather.mWeather.get(0);
                        MainPageAdapter mAdapter = new MainPageAdapter(mWeatherEntity);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        currentTemp.setText(String.format("%s℃", mWeatherEntity.now.tmp));

                        // Retrofit Request DEBUG PART
                        Log.e("测试", mWeatherEntity.toString());
                        break;
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(context, "网络请求失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("需要对您开放您的定位权限，否则部分功能无法正常使用");
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("更改权限", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName())); //根据程序包名打开对应设置界面
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    private void locationPermission() {
        if (!PermissionUtil.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            PermissionUtil.requestPermission(requestCode, this, Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            location();
        }
    }


    /**
     * 高德定位方法
     */
    private void location() {

//        mRefreshLayout.setRefreshing(true);
        //初始化定位
        mLocationClient = new AMapLocationClient(BaseApplication.getAppContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）ad
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔 单位毫秒
        int tempTime = SharedPreferenceUtil.getInstance().getAutoUpdate();
        if (tempTime == 0) {
            tempTime = 100;
        }
        mLocationOption.setInterval(tempTime * SharedPreferenceUtil.ONE_HOUR);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    location();
                } else {
                    requestPermission();
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 可以在OnResume处对title进行修改
        String cityName = SharedPreferenceUtil.getInstance().getCity();
        mToolbar.setTitle(cityName);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
        mLocationClient = null;
        mLocationOption = null;
    }

    @Override
    public void onBackPressed() {
        /* 对后退操作进行响应 */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!DoubleClickUtil.CC()) {
                Toast.makeText(this, R.string.double_click_exit, LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        // 将返回值设置为false, 则可以去掉NavigationView中点击的颜色显示
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_current_city:
                break;
            case R.id.nav_chosen_city:
                startActivity(new Intent(MainActivity.this, MultiCityActivity.class));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        /**
         * location方法()定位完成的回调，用定位城市替换当前默认城市
         */
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                aMapLocation.getLocationType();
                SharedPreferenceUtil
                        .getInstance()
                        .setCity(TextUtil.replaceCityName(aMapLocation.getCity()));
                Log.e("定位成功", SharedPreferenceUtil.getInstance().getCity());
            }
        }
    }
}
