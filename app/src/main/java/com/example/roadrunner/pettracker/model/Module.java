package com.example.roadrunner.pettracker.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by gnut3ll4 on 14/10/15.
 */
@DatabaseTable(tableName = "module")
public class Module {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    private String name;

    public Module() {
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

    public void setName(String name) {
        this.name = name;
    }
}
