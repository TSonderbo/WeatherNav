package com.example.weathernav.database.route;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.weathernav.database.BaseDao;
import com.example.weathernav.objects.Route;

import java.util.List;

@Dao
public interface RouteDao extends BaseDao<Route>
{
    @Query("SELECT * FROM routes")
    LiveData<List<Route>> getAllRoutes();

    @Query("DELETE FROM routes")
    void deleteAll();
}
