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

    @ForeignCollectionField(eager = true)
    Collection<Module> associatedModules;

    @DatabaseField
    boolean activated;

    @ForeignCollectionField(eager = true)
    Collection<Coordonnees> coordonnees;

    public Zone() {
    }

    public Zone(String name, boolean activated, Collection<Coordonnees> coordonnees) {
        this.name = name;
        this.associatedModules = new ArrayList<>();
        this.activated = activated;
        this.coordonnees = coordonnees;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void addModule(Module module) {
        this.associatedModules.add(module);
    }
    public void removeModule(Module module) {
        this.associatedModules.remove(module);
    }
}
