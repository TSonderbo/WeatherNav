package com.example.weathernav.network.weather;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.weathernav.objects.weatherdata.WeatherData;
import com.example.weathernav.objects.weatherdata.WeatherDataResponse;
import com.example.weathernav.util.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherAPIClient
{
    private WeatherAPI mApi;
    private MutableLiveData<List<WeatherData>> mData;

    public WeatherAPIClient()
    {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("http://api.weatherstack.com/") //TODO Change to some api
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = retrofitBuilder.build();
        mApi = retrofit.create(WeatherAPI.class);
        mData = new MutableLiveData<>();
        mData.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<WeatherData>> getWeather(double lat, double lon)
    {
        String apiKey = Constants.MY_WEATHER_API_KEY;
        Call<WeatherDataResponse> call = mApi.getWeather(apiKey, lat +","+ lon);
        call.enqueue(new Callback<WeatherDataResponse>()
        {
            @Override
            public void onResponse(Call<WeatherDataResponse> call, Response<WeatherDataResponse> response)
            {
                if(response.isSuccessful())
                {
                    List<WeatherData> temp = mData.getValue();
                    temp.add(response.body().getWeatherData());
                    mData.setValue(temp);
                }
                Log.i("Retrofit","Query successful");
                Log.i("Retrofit","Status code: " + response.code());
            }

            @Override
            public void onFailure(Call<WeatherDataResponse> call, Throwable t)
            {
                Log.i("Retrofit", "Something went wrong :<");
                Log.i("Retrofit", t.getLocalizedMessage());
                Log.i("Retrofit", t.toString());
            }
        });
        return mData;
    }

    public MutableLiveData<List<WeatherData>> getWeatherData()
    {
        return mData;
    }

    public void clearWeatherData()
    {
        mData.setValue(new ArrayList<>());
    }
}
