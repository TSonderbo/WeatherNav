package com.example.weathernav.util.listview;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.weathernav.R;
import com.example.weathernav.objects.weatherdata.WeatherData;

public class WeatherListAdapter extends ArrayAdapter<WeatherData>
{
    private int mResource;

    public WeatherListAdapter(@NonNull Context context, int resource)
    {
        super(context, resource);
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(mResource, parent, false);
        
        String temperature = convertView.getContext().getString(R.string.weather_adapter_temperature, getItem(position).getTemperature());
        String precipitation = convertView.getContext().getString(R.string.weather_adapter_precipitation, getItem(position).getPrecipitation());
        String location = getItem(position).getLocation();

        TextView textViewTemperature = (TextView) convertView.findViewById(R.id.text_weather_temperature);
        TextView textViewPrecipitation = (TextView) convertView.findViewById(R.id.text_weather_precipitation);
        TextView textViewUpper = (TextView) convertView.findViewById(R.id.text_weather_upper);

        textViewTemperature.setText(temperature);
        textViewPrecipitation.setText(precipitation);
        textViewUpper.setText(location);
        ImageView imageView = convertView.findViewById(R.id.image_weather);
        Glide.with(parent).load(getItem(position).getWeatherImage()).into(imageView);

        return convertView;
    }

    @Override
    public void addAll(WeatherData... items)
    {
        Log.i("WeatherListAdapter", "Added a list of size: " + items.length);
        super.addAll(items);
        notifyDataSetChanged();
    }


}
