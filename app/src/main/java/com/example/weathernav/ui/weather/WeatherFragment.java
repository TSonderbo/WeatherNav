package com.example.weathernav.ui.weather;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weathernav.R;
import com.example.weathernav.util.listview.WeatherListAdapter;

public class WeatherFragment extends Fragment
{
    private WeatherViewModel mViewModel;
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        mListView = root.findViewById(R.id.listView);
        mViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        Log.i("WeatherFragment", "Context = " + this.getContext());
        WeatherListAdapter adapter = new WeatherListAdapter(this.getContext(), R.layout.weather_list_item);
        mListView.setAdapter(adapter);

        mViewModel.getWeatherData().observe(getViewLifecycleOwner(), weatherData ->
        {
            Log.i("WeatherFragment", "Observed something");
            adapter.clear();
            adapter.addAll(weatherData);
        });
        return root;
    }
}
