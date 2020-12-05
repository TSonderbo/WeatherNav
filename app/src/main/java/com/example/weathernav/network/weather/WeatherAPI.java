package com.example.weathernav.network.weather;

import com.example.weathernav.objects.weatherdata.WeatherDataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI
{
    @GET("current")
    Call<WeatherDataResponse> getWeather(@Query("access_key") String apiKey, @Query("query") String latLong);
}
