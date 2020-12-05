package com.example.weathernav.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.weathernav.database.route.RouteDao;
import com.example.weathernav.database.route.RouteDatabase;
import com.example.weathernav.objects.Route;

import java.util.List;

public class RouteRepository
{
    private RouteDao mRouteDao;
    private static RouteRepository mInstance;
    private LiveData<List<Route>> mRoutes;

    private RouteRepository(Application application)
    {
        mRouteDao = RouteDatabase.getInstance(application).routeDao();
        mRoutes = mRouteDao.getAllRoutes();
    }

    public static synchronized RouteRepository getInstance(Application application)
    {
        if(mInstance == null)
        {
            mInstance = new RouteRepository(application);
        }
        return mInstance;
    }

    public void insert(Route route)
    {
        new InsertRouteAsync(mRouteDao).execute(route);
    }

    private static class InsertRouteAsync extends AsyncTask<Route, Void, Void>
    {
        private RouteDao mRouteDao;

        private InsertRouteAsync(RouteDao mRouteDao)
        {
            this.mRouteDao = mRouteDao;
        }

        @Override
        protected Void doInBackground(Route... routes)
        {
            mRouteDao.insert(routes[0]);
            return null;
        }
    }

    public void delete(Route route)
    {
        new DeleteRouteAsync(mRouteDao).execute(route);
    }

    public void deleteAll()
    {
        new DeleteRouteAsync(mRouteDao).execute();
    }

    private static class DeleteRouteAsync extends AsyncTask<Route, Void, Void>
    {
        private RouteDao mRouteDao;

        private DeleteRouteAsync(RouteDao routeDao)
        {
            this.mRouteDao = routeDao;
        }

        @Override
        protected Void doInBackground(Route... routes)
        {
            if(routes.length == 0)
            {
                mRouteDao.deleteAll();
            }
            else
            {
                mRouteDao.delete(routes[0]);
            }
            return null;
        }
    }


    public LiveData<List<Route>> getRoutes()
    {
        return mRoutes;
    }
}
