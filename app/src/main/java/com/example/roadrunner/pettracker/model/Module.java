package com.example.roadrunner.pettracker.model;

import com.example.roadrunner.pettracker.utils.MapsHelper;
import com.example.roadrunner.pettracker.utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Random;

/**
 * Created by gnut3ll4 on 14/10/15.
 */
@DatabaseTable(tableName = "module")
public class Module implements Comparable<Module> {

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

    public Module(String name, Coordonnees currentPosition) {
        this.name = name;
        this.currentPosition = currentPosition;
    }

    public Module(String name) {
        this.name = name;
        Random ran = new Random();
        double baseX = 45.494020, baseY = -73.563076;
        double diffX = 45.495314, diffY = -73.567303;

        double distance = MapsHelper.getDistanceBetween(new Coordonnees(baseX, baseY), new Coordonnees(diffX, diffY));


        double deltaX = distance * ran.nextDouble();
        double deltaY = distance * ran.nextDouble();

        currentPosition = new Coordonnees(baseX, baseY);
        //Random moving
        currentPosition.setLatitude(baseX + deltaX * (ran.nextBoolean() ? -1 : 1));
        currentPosition.setLongitude(baseY + deltaY * (ran.nextBoolean() ? -1 : 1));

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

        //Random moving
//        currentPosition.setLongitude(x + deltaX * (ran.nextBoolean() ? -1 : 1));
//        currentPosition.setLatitude(y + deltaY * (ran.nextBoolean() ? -1 : 1));

        currentPosition.setLongitude(x + deltaX);
        currentPosition.setLatitude(y + deltaY);
    }

    public Zone getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(Zone currentZone) {
        this.currentZone = currentZone;
    }

    public LatLng getLatLnt() {
        if (currentPosition == null) {
            return null;
        }
        return new LatLng(currentPosition.latitude, currentPosition.longitude);
    }

    @Override
    public int compareTo(Module anotherModule) {
        if(this.id == anotherModule.id)
            return 0;
        else
            return -1;
    }
}
