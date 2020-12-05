package com.example.weathernav.objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "routes")
public class Route
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double kilometers;
    private String from;
    private String to;

    public Route(double kilometers, String from, String to)
    {
        this.kilometers = kilometers;
        this.from = from;
        this.to = to;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getKilometers()
    {
        return kilometers;
    }

    public void setKilometers(double kilometers)
    {
        this.kilometers = kilometers;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }
}
