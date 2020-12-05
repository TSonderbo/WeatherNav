package com.example.weathernav.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.weathernav.network.weather.WeatherAPIClient;
import com.example.weathernav.objects.weatherdata.WeatherData;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class WeatherRepository
{
    private static WeatherRepository mInstance;
    private WeatherAPIClient mWeatherAPIClient;
    private List<WeatherData> mWeatherData;

    private WeatherRepository()
    {
        mWeatherAPIClient = new WeatherAPIClient();
        mWeatherData = new ArrayList<>();
    }

    public static WeatherRepository getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new WeatherRepository();
        }
        return mInstance;
    }

    public MutableLiveData<List<WeatherData>> getWeatherData()
    {
        return mWeatherAPIClient.getWeatherData();
    }

    public void calculateWeatherForMarkers(List<Marker> markerList)
    {
        for(Marker marker : markerList)
        {
            mWeatherAPIClient.getWeather(
                    marker.getPosition().latitude,
                    marker.getPosition().longitude
            );
        }
    }

    public void clearWeatherData()
    {
        mWeatherAPIClient.clearWeatherData();
    }

}
