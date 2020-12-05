package com.example.weathernav.database.route;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.weathernav.objects.Route;

@Database(entities = Route.class, version = 1)
public abstract class RouteDatabase extends RoomDatabase
{
    private static RouteDatabase mInstance;
    public abstract RouteDao routeDao();

    public static synchronized RouteDatabase getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = Room.databaseBuilder(context.getApplicationContext(),
                    RouteDatabase.class, "routes")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }
}
