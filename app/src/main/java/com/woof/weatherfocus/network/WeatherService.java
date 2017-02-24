package com.woof.weatherfocus.network;

import com.woof.weatherfocus.model.entity.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Woof on 2/23/2017.
 */

public interface WeatherService {
    static final String baseUrl = "https://free-api.heweather.com/v5/";
    @GET("weather")
    Call<Weather> getWeather(@Query("city") String city, @Query("key") String key);
}
