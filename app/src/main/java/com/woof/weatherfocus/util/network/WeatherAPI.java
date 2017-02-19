package com.woof.weatherfocus.util.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.woof.weatherfocus.model.entity.WeatherEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Woof on 2/15/2017.
 */
public class WeatherAPI {
    @SerializedName("HeWeather5")@Expose
    public List<WeatherEntity> mWeatherDataService = new ArrayList<>();
}
