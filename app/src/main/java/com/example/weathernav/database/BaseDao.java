package com.example.weathernav.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

public interface BaseDao<TEntity>
{
    @Insert
    void insert(TEntity entity);

    @Insert
    void insert(List<TEntity> entities);

    @Update
    void update(TEntity entity);

    @Update
    void update(List<TEntity> entities);

    @Delete
    void delete(TEntity entity);

    @Delete
    void delete(List<TEntity> entities);
}
