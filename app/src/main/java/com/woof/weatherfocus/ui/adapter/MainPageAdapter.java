package com.woof.weatherfocus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.woof.weatherfocus.R;
import com.woof.weatherfocus.base.AnimRecyclerViewAdapter;
import com.woof.weatherfocus.base.BaseViewHolder;
import com.woof.weatherfocus.model.entity.WeatherEntity;
import com.woof.weatherfocus.util.GlideHelper;
import com.woof.weatherfocus.util.SharedPreferenceUtil;
import com.woof.weatherfocus.util.TextUtil;
import com.woof.weatherfocus.util.TimeUtil;

import butterknife.BindView;

/**
 * 该类为主界面的天气显示界面，涉及到多个类型的item的布局
 * Created by Woof on 3/4/2017.
 */

public class MainPageAdapter extends AnimRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private WeatherEntity weatherData;

    // 用于运行调试
    private static final String TAG = MainPageAdapter.class.getSimpleName();

    // 布局类型
    private static final int TYPE_MAIN = 0;
    private static final int TYPE_SUGGESTION = 1;
    private static final int TYPE_DAYS = 2;

    public MainPageAdapter(WeatherEntity weatherData) {
        this.weatherData = weatherData;
    }

    /**
     * 该方法通过传入的 postion 判断item的类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return MainPageAdapter.TYPE_MAIN;
            case 1:
                return MainPageAdapter.TYPE_SUGGESTION;
            case 2:
                return MainPageAdapter.TYPE_DAYS;
        }
        return super.getItemViewType(position);
    }

    /**
     * 创建item布局
     * ViewHolder的layout布局加载每个type对应的父viewgroup
     * 如果加载时出现无法匹配会报空指针异常
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 获取布局的Context
        mContext = parent.getContext();
        switch (viewType){
            case TYPE_MAIN:
                return new MainViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.city_detail_item, parent, false));
            case TYPE_SUGGESTION:
                return new SuggestionViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.suggestion_item, parent, false));
            case TYPE_DAYS:
                return new DaysViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.forecast_item, parent, false));
        }
        return null;
    }

    /**
     * 进行数据绑定，已经一些事件点击操作，可以将数据绑定部分转移到各对应的viewholder当中，
     * 让BindViewHolder中的内容更为轻量
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int itemType = getItemViewType(position);
        switch (itemType) {
            case TYPE_MAIN:
                ((MainViewHolder) holder).bind(weatherData);
                break;
            case TYPE_SUGGESTION:
                ((SuggestionViewHolder) holder).bind(weatherData);
                break;
            case TYPE_DAYS:
                    ((DaysViewHolder) holder).bind(weatherData);
            default:
                break;
        }
    }

    /**
     * 此处当前写法非常不好，当忘记修改数目时，debug出现异常
     * Attempt to write to field 'int android.support.v7.widget.RecyclerView$ViewHolder.mItemViewType'
     * on a null object reference
     * 可以通过三目运算符对数据返回的status进行判断,暂定位固定数值
     * @return
     */
    @Override
    public int getItemCount() {
        return 3;
    }

    class MainViewHolder extends BaseViewHolder<WeatherEntity> {
        @BindView(R.id.weather_icon)
        ImageView weatherIcon;
        @BindView(R.id.current_temp)
        TextView currentTemp;
        @BindView(R.id.high_temp)
        TextView highTemp;
        @BindView(R.id.low_temp)
        TextView lowTemp;
        @BindView(R.id.pm_info)
        TextView pmInfo;
        @BindView(R.id.air_info)
        TextView airInfo;

        MainViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 绑定方法部分可以通过读取SharedPreference Config文件做天气状况判断逻辑
         * bind方法主要是实现view视图的绑定，完成view的部分
         * @param weather
         */
        public void bind(WeatherEntity weather) {
            // 当前气温
            currentTemp.setText(String.format("%s℃", weather.now.tmp));
            // 当日最高温度
            highTemp.setText(String.format("↑ %s ℃", weather.dailyForecast.get(0).tmp.max));
            // 当日最低温度
            lowTemp.setText(String.format("↓ %s ℃", weather.dailyForecast.get(0).tmp.min));
            // 当日PM2.5
            pmInfo.setText(String.format("PM2.5: %s μg/m³", TextUtil.setText("" + weather.aqi.city.pm25)));
            // 当日空气质量
            airInfo.setText(TextUtil.setText("空气质量: ", weather.aqi.city.qlty));
            // 加载天气图标
            GlideHelper.imageLoad(itemView.getContext(), SharedPreferenceUtil.getInstance().
                    getInt(weather.now.cond.txt, R.mipmap.none_respond), weatherIcon);

            Log.e("调用bind", "测试");
        }
    }

    class SuggestionViewHolder extends BaseViewHolder<WeatherEntity> {

        @BindView(R.id.cloth_brief)
        TextView clothBrief;
        @BindView(R.id.cloth_txt)
        TextView clothContent;
        @BindView(R.id.flu_brief)
        TextView fluBrief;
        @BindView(R.id.flu_txt)
        TextView fluContent;
        @BindView(R.id.sport_brief)
        TextView sportBrief;
        @BindView(R.id.sport_txt)
        TextView sportContent;
        @BindView(R.id.uv_brief)
        TextView uvBrief;
        @BindView(R.id.uv_txt)
        TextView uvContent;

        SuggestionViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(WeatherEntity weather) {
            clothBrief.setText(weather.suggestion.drsg.brf);
            clothContent.setText(weather.suggestion.drsg.txt);
            fluBrief.setText(weather.suggestion.flu.brf);
            fluContent.setText(weather.suggestion.flu.txt);
            sportBrief.setText(weather.suggestion.sport.brf);
            sportContent.setText(weather.suggestion.sport.txt);
            uvBrief.setText(weather.suggestion.uv.brf);
            uvContent.setText(weather.suggestion.uv.txt);

            Log.e("调用bind", "绑定成功");
        }

    }

    class DaysViewHolder extends BaseViewHolder<WeatherEntity> {

        private LinearLayout forecastContainer;
        private TextView[]  forecastDate = new TextView[weatherData.dailyForecast.size()];
        private TextView[]  forecastTemp = new TextView[weatherData.dailyForecast.size()];
        private TextView[]  forecastTxt  = new TextView[weatherData.dailyForecast.size()];
        private ImageView[] forecastIcon = new ImageView[weatherData.dailyForecast.size()];

        /**
         * ViewHolder当中对item group内部控件进行初始化操作
         * @param itemView
         */
        DaysViewHolder(View itemView) {
            super(itemView);

            forecastContainer = ((LinearLayout) itemView.findViewById(R.id.forecast_linear));
            for (int i = 0; i < weatherData.dailyForecast.size(); i++) {
                View view = View.inflate(mContext, R.layout.forecast_info_item, null);
                forecastIcon[i] = ((ImageView) view.findViewById(R.id.forecast_icon));
                forecastDate[i] = ((TextView)  view.findViewById(R.id.forecast_date));
                forecastTemp[i] = ((TextView)  view.findViewById(R.id.forecast_temp));
                forecastTxt[i]  = ((TextView)  view.findViewById(R.id.forecast_txt));
                forecastContainer.addView(view);
            }
        }

        public void bind(WeatherEntity weather) {

            forecastDate[0].setText("今天");
            forecastDate[1].setText("明天");

            for (int i = 0; i < weatherData.dailyForecast.size(); i++){
                if (i > 1) {
                    try {
                        forecastDate[i].setText(TimeUtil.dayForWeek(weather.dailyForecast.get(i).date));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                GlideHelper.imageLoad(
                        mContext, SharedPreferenceUtil.getInstance().
                        getInt(weather.dailyForecast.get(i).cond.txtD, R.mipmap.none_respond),
                        forecastIcon[i]);
                forecastTemp[i].setText(String.format("%s℃ - %s℃",
                        weather.dailyForecast.get(i).tmp.min,
                        weather.dailyForecast.get(i).tmp.max));
                forecastTxt[i].setText(String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
                        weather.dailyForecast.get(i).cond.txtD,
                        weather.dailyForecast.get(i).wind.sc,
                        weather.dailyForecast.get(i).wind.dir,
                        weather.dailyForecast.get(i).wind.spd,
                        weather.dailyForecast.get(i).pop));
            }
        }
    }
}
