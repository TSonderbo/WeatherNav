package com.example.weathernav.ui.weather;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weathernav.objects.weatherdata.WeatherData;
import com.example.weathernav.repositories.WeatherRepository;

import java.util.List;

public class WeatherViewModel extends ViewModel
{
    private WeatherRepository mWeatherRepository;
    public WeatherViewModel()
    {
        mWeatherRepository = WeatherRepository.getInstance();
    }

    public MutableLiveData<List<WeatherData>> getWeatherData()
    {
        return mWeatherRepository.getWeatherData();
    }
}
