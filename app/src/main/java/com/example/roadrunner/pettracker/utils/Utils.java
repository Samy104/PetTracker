package com.example.roadrunner.pettracker.utils;

import android.graphics.Color;
import android.location.Location;

import com.example.roadrunner.pettracker.model.Coordonnees;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.model.Zone;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by gnut3ll4 on 30/10/15.
 */
public class Utils {



    public static int stringToColour(String str) {
        int hash = str.hashCode();

        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;

        return Color.argb(130, r, g, b);


    }
}
