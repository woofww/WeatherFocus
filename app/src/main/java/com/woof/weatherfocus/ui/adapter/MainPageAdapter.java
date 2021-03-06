package com.woof.weatherfocus.ui.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.woof.weatherfocus.util.ChartStyleUtil;
import com.woof.weatherfocus.util.GlideHelper;
import com.woof.weatherfocus.util.SharedPreferenceUtil;
import com.woof.weatherfocus.util.TemperatureTransUtil;
import com.woof.weatherfocus.util.TextUtil;
import com.woof.weatherfocus.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

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
    private static final int TYPE_CHART = 3;

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
                return MainPageAdapter.TYPE_DAYS;
            case 2:
                return MainPageAdapter.TYPE_CHART;
            case 3:
                return MainPageAdapter.TYPE_SUGGESTION;

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
            case TYPE_CHART:
                return new ChartViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.forecast_linechart_item, parent, false));
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
                break;
            case TYPE_CHART:
                ((ChartViewHolder) holder).bind(weatherData);
            default:
                break;
        }
        if (SharedPreferenceUtil.getInstance().getAnimationSwitch()) {
            showItemAnim(holder.itemView, position);
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
        return 4;
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
            // 部分地区空气质量指数不存在，分别处理
            if (weather.aqi != null){
                // 当日PM2.5
                pmInfo.setText(String.format("PM2.5: %s μg/m³", TextUtil.setText("" + weather.aqi.city.pm25)));
                // 当日空气质量
                airInfo.setText(TextUtil.setText("空气质量: ", weather.aqi.city.qlty));
            } else {
                pmInfo.setText("");
                airInfo.setText("");
            }
            // 加载天气图标
            GlideHelper.imageLoad(itemView.getContext(), SharedPreferenceUtil.getInstance().
                    getInt(weather.now.cond.txt, R.mipmap.none_respond), weatherIcon);
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
        @BindView(R.id.washcar_brief)
        TextView carBrief;
        @BindView(R.id.washcar_txt)
        TextView carContent;



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
            carBrief.setText(weather.suggestion.cw.brf);
            carContent.setText(weather.suggestion.cw.txt);
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
            for (int i = 0; i < weatherData.dailyForecast.size() - 2; i++) {
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

            for (int i = 0; i < weatherData.dailyForecast.size() - 2; i++){
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

    class ChartViewHolder extends BaseViewHolder<WeatherEntity> {

        @BindView(R.id.chart)
        LineChartView mLineCharView;

        ChartStyleUtil mChartStyleUtil = new ChartStyleUtil();
        Axis mAxisX = new Axis();
        Axis mAxisY = new Axis().setHasLines(true);
         LineChartData mChartData = new LineChartData();

        // 用于保存在图当中显示的曲线，HelloChart的库最多显示四条
        private List<Line> mLines = new ArrayList<>();
        private List<PointValue> mPointValueMax = new ArrayList<>();
        private List<PointValue> mPointValueMin = new ArrayList<>();
//        private List<AxisValue> mAxisValue = new ArrayList<>();

        private final int numberOfLines = 2;

        ChartViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 对图当中的数据进行填充
         * @param weather
         */
        @Override
        public void bind(WeatherEntity weather) {

            AxisLabel(weather); // 标注坐标轴
            mChartData = mChartStyleUtil.getChartData(initDataSet(weather)); // 获取布局填充数据
            mChartStyleUtil.setAxis(mAxisX, mAxisY, AxisLabel(weather), mChartData); // 设置样式
            initLineChart(mChartData);


        }

        private void initLineChart(LineChartData lineChartData) {
            mLineCharView.setLineChartData(lineChartData);
            mLineCharView.setInteractive(true);
            mLineCharView.setVisibility(View.VISIBLE);
            mLineCharView.setViewportCalculationEnabled(true);
            mLineCharView.setZoomType(ZoomType.HORIZONTAL); // 缩放类型，水平
            mLineCharView.setMaxZoom((float) 3);// 缩放比例
            Viewport viewport = new Viewport(mLineCharView.getMaximumViewport());
            viewport.left = 0;
            viewport.right = 7;
            mLineCharView.setCurrentViewport(viewport);
        }

        private List<Line> initDataSet(WeatherEntity weather) {

            /**
             * 由于曲线图中存在两条线，分别为最高温趋势线和最低温趋势线，
             * 分两个case进行操作，将图的数据存储到分别存储到各自的line当中
             */
            for (int i = 0; i < numberOfLines; i++) {
                switch (i) {
                    case 0:
                        /**
                         * 添加数据集,通过parseFloat对天气字符字符串进行转型
                         */
                        for (int j = 0; j < AxisLabel(weather).size(); j++) {
                            // 添加最高温度数据集
                            mPointValueMax.add(new PointValue(j, Float.parseFloat(weather
                                    .dailyForecast
                                    .get(j).tmp.max)));
                        }
                        Line lineMax = new Line(mPointValueMax);
                        mLines.add(mChartStyleUtil.getChartStyle(Color.parseColor("#E64A19"), lineMax));
                        break;
                    case 1:
                        for (int j = 0; j < AxisLabel(weather).size(); j++) {
                            // 添加最低温度数据集
                            mPointValueMin.add(new PointValue(j, Float.parseFloat(weather
                                    .dailyForecast
                                    .get(j).tmp.min)));
                        }
                        Line lineMin = new Line(mPointValueMin);
                        mLines.add(mChartStyleUtil.getChartStyle(Color.parseColor("#ff9800"), lineMin));
                        break;
                }
            }
            int i = AxisLabel(weather).size();
            Log.e("是否存在数据", "" + i);
            return mLines;
        }

        private List<AxisValue> AxisLabel(WeatherEntity weatherEntity) {

            /**
             * 对x轴的坐标进行标注,同时获取x轴坐标列表
             */
            List<AxisValue> axisValue = new ArrayList<>();
            axisValue.add(new AxisValue(0).setLabel("今日"));
            axisValue.add(new AxisValue(1).setLabel("明日"));

            for (int i = 0; i < weatherData.dailyForecast.size(); i++) {
                // 该部分用于对轴坐标进行更新
                if (i > 1) {
                    try {
                        axisValue.add(new AxisValue(i)
                                .setLabel(TimeUtil.dayForWeek(weatherEntity.dailyForecast.get(i).date)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return axisValue;
        }

    }
}
