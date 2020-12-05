package com.example.weathernav.ui.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.weathernav.objects.Route;
import com.example.weathernav.repositories.RouteRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel
{
    private RouteRepository mRouteRepository;

    public HistoryViewModel(@NonNull Application application)
    {
        super(application);
        mRouteRepository = RouteRepository.getInstance(application);
    }

    public LiveData<List<Route>> getRouteHistory()
    {
        return mRouteRepository.getRoutes();
    }
}
