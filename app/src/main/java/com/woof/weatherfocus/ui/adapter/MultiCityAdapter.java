package com.woof.weatherfocus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.woof.weatherfocus.R;
import com.woof.weatherfocus.base.BaseViewHolder;
import com.woof.weatherfocus.model.entity.WeatherEntity;
import com.woof.weatherfocus.util.GlideHelper;
import com.woof.weatherfocus.util.SharedPreferenceUtil;
import com.woof.weatherfocus.util.TextUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Woof on 4/9/2017.
 */

public class MultiCityAdapter extends RecyclerView.Adapter<MultiCityAdapter.MultiCityViewHolder> {

    private String TAG = MultiCityAdapter.class.getSimpleName();
    private onMultiCityLongClick mOnMultiCityLongClick = null;
    private List<WeatherEntity> mWeatherList;
    private Context mContext;

    public MultiCityAdapter(List<WeatherEntity> weatherList) {
        mWeatherList = weatherList;
    }

    @Override
    public MultiCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MultiCityViewHolder(LayoutInflater.from(mContext).inflate(R.layout.multi_city_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MultiCityViewHolder holder, final int position) {
        holder.bind(mWeatherList.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnMultiCityLongClick.longClick(mWeatherList.get(position).basic.city);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

    public boolean isEmpty() {
        return 0 == mWeatherList.size();
    }

    class MultiCityViewHolder extends BaseViewHolder<WeatherEntity> {

        @BindView(R.id.air_info_multicity)
        TextView airInfo;
        @BindView(R.id.pm_info_multicity)
        TextView pm;
        @BindView(R.id.current_temp_multicity)
        TextView currentTemp;
        @BindView(R.id.high_temp_multicity)
        TextView highTemp;
        @BindView(R.id.low_temp_multicity)
        TextView lowTemp;
        @BindView(R.id.cardView_multiCity)
        CardView mCardView;
        @BindView(R.id.city_name)
        TextView cityName;
        @BindView(R.id.weather_icon_multicity)
        ImageView weatherIcon;

        public MultiCityViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(WeatherEntity weatherEntity) {
            try {
                cityName.setText(TextUtil.setText(weatherEntity.basic.city));
                currentTemp.setText(String.format("%s℃", weatherEntity.now.tmp));
                // 当日最高温度
                highTemp.setText(String.format("↑ %s ℃", weatherEntity.dailyForecast.get(0).tmp.max));
                // 当日最低温度
                lowTemp.setText(String.format("↓ %s ℃", weatherEntity.dailyForecast.get(0).tmp.min));

            } catch (NullPointerException e) {
                Log.e(TAG, "出现错误");
            }
            // 加载天气图标
            GlideHelper.imageLoad(itemView.getContext(), SharedPreferenceUtil.getInstance().
                    getInt(weatherEntity.now.cond.txt, R.mipmap.none_respond), weatherIcon);
        }

    }

    public interface  onMultiCityLongClick {

        void longClick(String city);
    }

    public void setOnMultiCityLongClick(onMultiCityLongClick onMultiCityLongClick) {
        this.mOnMultiCityLongClick = onMultiCityLongClick;
    }
}
