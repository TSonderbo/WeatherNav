package com.example.weathernav.ui.navigation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.weathernav.objects.Route;
import com.example.weathernav.repositories.RouteRepository;
import com.example.weathernav.repositories.WeatherRepository;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;

import java.util.List;

public class NavigationViewModel extends AndroidViewModel
{
    //Repositories
    private WeatherRepository mWeatherRepo;
    private RouteRepository mRouteRepo;

    public NavigationViewModel(@NonNull Application application)
    {
        super(application);
        mWeatherRepo = WeatherRepository.getInstance();
        mRouteRepo = RouteRepository.getInstance(application);
    }

    public void calculateWeatherForMarkers(List<Marker> mMarkerList)
    {
        mWeatherRepo.calculateWeatherForMarkers(mMarkerList);
    }

    public void clearWeatherForMarkers()
    {
        mWeatherRepo.clearWeatherData();
    }

    public void saveRoute(DirectionsRoute dRoute)
    {
        int totalDistance = 0;
        for (DirectionsLeg leg : dRoute.legs)
        {
            totalDistance += (leg.distance.inMeters / 1000);
        }
        String from = dRoute.legs[0].startAddress;
        String to = dRoute.legs[dRoute.legs.length - 1].endAddress;
        Route route = new Route(totalDistance, from, to);
        mRouteRepo.insert(route);
    }
}
