package com.example.roadrunner.pettracker.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by gnut3ll4 on 14/10/15.
 */

@DatabaseTable(tableName = "coordonnees")
public class Coordonnees {


    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    double latitude;

    @DatabaseField
    double longitude;

    public Coordonnees() {
    }

    public Coordonnees(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
