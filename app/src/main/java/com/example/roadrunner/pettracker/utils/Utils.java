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

    public static void createTestDatas(DatabaseHelper databaseHelper) {

        //45.494323, -73.559929 = À côté de l'ÉTS

        try {
            Dao<Module, ?> moduleDao = databaseHelper.getDao(Module.class);
            Dao<Zone, ?> zoneDao = databaseHelper.getDao(Zone.class);
            Dao<Coordonnees, ?> coordonneesDao = databaseHelper.getDao(Coordonnees.class);

            ConnectionSource connectionSource = databaseHelper.getConnectionSource();
            TableUtils.clearTable(connectionSource, Module.class);
            TableUtils.clearTable(connectionSource, Zone.class);
            TableUtils.clearTable(connectionSource, Coordonnees.class);

            Module rexModule = new Module("Rex", new Coordonnees(45.494323, -73.559929));
            Module chienModule = new Module("Chien", new Coordonnees(45.495430, -73.563064));

            ArrayList<Module> modules = new ArrayList<>();

            modules.add(rexModule);
            modules.add(chienModule);


            ArrayList<Coordonnees> residencesCoordonnees = new ArrayList<>();
            residencesCoordonnees.add(new Coordonnees(45.495293, -73.562927));
            residencesCoordonnees.add(new Coordonnees(45.494802, -73.561291));
            residencesCoordonnees.add(new Coordonnees(45.495626, -73.560629));
            residencesCoordonnees.add(new Coordonnees(45.496081, -73.562611));

            ArrayList<Coordonnees> mdeCoordonnees = new ArrayList<>();
            mdeCoordonnees.add(new Coordonnees(45.493787, -73.563462));
            mdeCoordonnees.add(new Coordonnees(45.494283, -73.563773));

            ArrayList<Zone> zones = new ArrayList<>();

            Zone residences = new Zone("Residences", true, residencesCoordonnees);
            residences.addModule(modules.get(0));
            modules.get(0).setCurrentZone(residences);
            for(Coordonnees coordonnees : residencesCoordonnees) {
                coordonnees.setZone(residences);
            }
            zones.add(residences);

            Zone mde = new Zone("Maison des étudiants", true, mdeCoordonnees);
            mde.addModule(modules.get(1));
            modules.get(1).setCurrentZone(mde);
            for(Coordonnees coordonnees : mdeCoordonnees) {
                coordonnees.setZone(mde);
            }
            zones.add(mde);


            for(Module module : modules) {
                moduleDao.createOrUpdate(module);
            }

            for(Coordonnees coordonnees : residencesCoordonnees) {
                coordonneesDao.createOrUpdate(coordonnees);
            }

            for(Coordonnees coordonnees : mdeCoordonnees) {
                coordonneesDao.createOrUpdate(coordonnees);
            }


            for (Zone zone : zones) {
                zoneDao.createOrUpdate(zone);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static float getDistanceBetween(Coordonnees coordonnees1, Coordonnees coordonnees2) {

        float[] distances = new float[1];

        Location.distanceBetween(
                coordonnees1.getLatitude(),
                coordonnees1.getLongitude(),
                coordonnees2.getLatitude(),
                coordonnees2.getLongitude(),
                distances
        );

        return distances[0];
    }

    public static int stringToColour(String str) {
        int hash = str.hashCode();

        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;

        return Color.argb(130, r, g, b);


    }
}
