package com.example.weatherdemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("weather")
    Call<model> getWeather(@Query("q") String cityName,
                           @Query("appid") String appid,
                           @Query("units") String units);
}
