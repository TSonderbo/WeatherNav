package com.example.weathernav.objects.weatherdata;

public class WeatherData
{
    private double precipitation;
    private double temperature;
    private String location;
    private String weatherImage;
    private String description;

    public WeatherData(double precipitation, double temperature, String location, String weatherImage, String description)
    {
        this.precipitation = precipitation;
        this.temperature = temperature;
        this.location = location;
        this.weatherImage = weatherImage;
        this.description = description;
    }

    public WeatherData()
    {
        this.precipitation = 0;
        this.temperature = 0;
        this.location = "Error";
        this.weatherImage = "";
        this.description = "Error";
    }

    public double getPrecipitation()
    {
        return precipitation;
    }

    public double getTemperature()
    {
        return temperature;
    }

    public String getLocation()
    {
        return location;
    }

    public String getWeatherImage()
    {
        return weatherImage;
    }

    public String getDescription()
    {
        return description;
    }
}
