package com.example.weathernav.objects.weatherdata;

import com.google.gson.annotations.SerializedName;

public class WeatherDataResponse
{
    private Request request;
    private Location location;
    private Current current;
    private ErrorCode error;

    public WeatherDataResponse()
    {
        request = new Request();
        location = new Location();
        current = new Current();
        error = new ErrorCode();
    }

    public WeatherData getWeatherData()
    {
        String icon = current.weather_icons.size() >= 1 ? current.weather_icons.get(0) : null;
        String description = current.weather_descriptions.size() >= 1 ? current.weather_descriptions.get(0) : null;
        return new WeatherData(current.precip,
                current.temperature,
                location.name,
                icon,
                description);
    }
}
