package com.example.roadrunner.pettracker.utils;

import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;

import com.example.roadrunner.pettracker.model.Coordonnees;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.model.Zone;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by gnut3ll4 on 30/10/15.
 */
public class MapsHelper {

    public static void displayZones(GoogleMap map, List<Zone> zones) {

        for (Zone zone : zones) {

            ArrayList<Coordonnees> coordonnees = new ArrayList<Coordonnees>(zone.getCoordonnees());
            Collection<Module> associatedModules = zone.getAssociatedModules();

            if (coordonnees.size() == 2) { //If this is a circle
                Circle circle = map.addCircle(new CircleOptions()
                        .center(coordonnees.get(0).getLatLng())
                        .radius(getRadius(coordonnees.get(0), coordonnees.get(1)))
                        .strokeColor(Color.BLACK)
                        .fillColor(Utils.stringToColour(zone.getName())));
            } else if (coordonnees.size() > 2) { // If this is a polygon
                PolygonOptions polygon = new PolygonOptions();
                for (Coordonnees coord : coordonnees) {
                    polygon.add(coord.getLatLng());
                }
                polygon.fillColor(Utils.stringToColour(zone.getName()));
                map.addPolygon(polygon);

            }

            for (Module module : zone.getAssociatedModules()) {

                module.getCurrentPosition();
            }

        }

    }

    public static boolean isPointInCircle(LatLng center, double radius, LatLng point) {
        double center_x = center.latitude;
        double center_y = center.longitude;
        double x = point.latitude;
        double y = point.longitude;

        double square_dist = Math.pow(center_x - x, 2) + Math.pow(center_y - y, 2);
        return square_dist <= Math.pow(radius, 2);

    }


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

            Module rexModule = new Module("Rex", new Coordonnees(45.495346, -73.561865));
            Module chienModule = new Module("Médor", new Coordonnees(45.493793, -73.563432));

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
            for (Coordonnees coordonnees : residencesCoordonnees) {
                coordonnees.setZone(residences);
            }
            zones.add(residences);

            Zone mde = new Zone("Maison des étudiants", true, mdeCoordonnees);
            mde.addModule(modules.get(1));
            modules.get(1).setCurrentZone(mde);
            for (Coordonnees coordonnees : mdeCoordonnees) {
                coordonnees.setZone(mde);
            }
            zones.add(mde);


            for (Module module : modules) {
                moduleDao.createOrUpdate(module);
            }

            for (Coordonnees coordonnees : residencesCoordonnees) {
                coordonneesDao.createOrUpdate(coordonnees);
            }

            for (Coordonnees coordonnees : mdeCoordonnees) {
                coordonneesDao.createOrUpdate(coordonnees);
            }


            for (Zone zone : zones) {
                zoneDao.createOrUpdate(zone);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static double getDistanceBetween(Coordonnees coordonnees1, Coordonnees coordonnees2) {


        double distance = Math.sqrt(
                Math.pow(coordonnees1.getLatitude() - coordonnees2.getLatitude(), 2) +
                        Math.pow(coordonnees1.getLongitude() - coordonnees2.getLongitude(), 2)
        );

        return distance;
    }


    public static float getRadius(Coordonnees coordonnees1, Coordonnees coordonnees2) {

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


}
