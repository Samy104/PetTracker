package com.example.roadrunner.pettracker.model;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Random;

/**
 * Created by gnut3ll4 on 14/10/15.
 */
@DatabaseTable(tableName = "module")
public class Module {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    String name;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "coordonnees_id")
    Coordonnees currentPosition;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = "zone_id")
    Zone currentZone;

    public Module() {
    }

    public Module(Coordonnees initial) {
        currentPosition = initial;
    }

    public Module(String name, Coordonnees currentPosition) {
        this.name = name;
        this.currentPosition = currentPosition;
    }

    public Module(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordonnees getCurrentPosition() {
        return currentPosition;
    }

    public void setName(String name) {
        this.name = name;
    }

    //This method will randomize a position relative to the current position
    public void generateNewPosition() {
        Random ran = new Random();
        double x = currentPosition.getLongitude();
        double y = currentPosition.getLatitude();

        double deltaX = ran.nextDouble() / 5000;
        double deltaY = ran.nextDouble() / 5000;

        currentPosition.setLongitude(x + deltaX * (ran.nextBoolean() ? -1 : 1));
        currentPosition.setLatitude(y + deltaY * (ran.nextBoolean() ? -1 : 1));
    }

    public Zone getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(Zone currentZone) {
        this.currentZone = currentZone;
    }

    public LatLng getLatLnt() {
        return new LatLng(currentPosition.latitude, currentPosition.longitude);
    }
}
