package com.example.roadrunner.pettracker.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by gnut3ll4 on 14/10/15.
 */

@DatabaseTable(tableName = "zone")
public class Zone {


    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    String name;

    @ForeignCollectionField(eager = false)
    Collection<Module> associatedModules;

    @DatabaseField
    boolean activated;

    @ForeignCollectionField(eager = false)
    Collection<Coordonnees> coordonnees;

    public Zone() {
    }

    public Zone(String name, Collection<Module> associatedModules, boolean activated, Collection<Coordonnees> coordonnees) {
        this.name = name;
        this.associatedModules = associatedModules;
        this.activated = activated;
        this.coordonnees = coordonnees;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Module> getAssociatedModules() {
        return associatedModules;
    }

    public void setAssociatedModules(Collection<Module> associatedModules) {
        this.associatedModules = associatedModules;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Collection<Coordonnees> getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(Collection<Coordonnees> coordonnees) {
        this.coordonnees = coordonnees;
    }
}
