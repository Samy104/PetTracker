package com.example.roadrunner.pettracker.model;

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
    private String name;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Coordonnees currentPosition;

    public Module() {
        currentPosition = new Coordonnees(45.521705, -73.577743);
    }

    public Module(Coordonnees initial) {
        currentPosition = initial;
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

    public Coordonnees getCurrentPosition() { return currentPosition; }

    public void setName(String name) {
        this.name = name;
    }

    //This method will randomize a position relative to the current position
    public void generateNewPosition()
    {
        Random ran = new Random();
        double x = currentPosition.getLongitude();
        double y = currentPosition.getLatitude();

        double deltaX = x + ran.nextDouble()/1000;
        double deltaY = y + ran.nextDouble()/1000;

        currentPosition.setLongitude(x + deltaX);
        currentPosition.setLatitude(y + deltaY);
    }
}
