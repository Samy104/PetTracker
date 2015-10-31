package com.example.roadrunner.pettracker.utils;

import android.graphics.Color;
import android.location.Location;

import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Coordonnees;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.model.Zone;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by gnut3ll4 on 30/10/15.
 */
public class MapsHelper {
    Marker marker = new Marker(null);
    public static void displayZones(final GoogleMap map, List<Zone> zones) {


        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(37.35, -122.0))
                .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
                .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
                .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
                .add(new LatLng(37.35, -122.0)); // Closes the polyline.

// Get back the mutable Polyline
        Polyline polyline = map.addPolyline(rectOptions);
        Marker marker = null;
        MultiListener ml = new MultiListener();

        for (Zone zone : zones) {

            ArrayList<Coordonnees> coordonnees = new ArrayList<Coordonnees>(zone.getCoordonnees());
            Collection<Module> associatedModules = zone.getAssociatedModules();

            if (coordonnees.size() == 2) { //If this is a circle
                final Circle circle = map.addCircle(new CircleOptions()
                        .center(coordonnees.get(0).getLatLng())
                        .radius(Utils.getDistanceBetween(coordonnees.get(0), coordonnees.get(1)))
                        .strokeColor(Color.BLACK)
                        .fillColor(Utils.stringToColour(zone.getName())));
                //listener sur de clic sur la carte qui active un marker si on clique dans une zone circulaire
                ml.add(new MarkersOnClickListener(circle, map, marker));
            } else if (coordonnees.size() > 2) { // If this is a polygon
                System.out.println("yo1");
                PolygonOptions polygon = new PolygonOptions();
                for (Coordonnees coord : coordonnees) {
                    polygon.add(coord.getLatLng());
                }
                polygon.fillColor(Utils.stringToColour(zone.getName()));
                map.addPolygon(polygon);
                ml.add(new MarkersOnClickListenerPolygon(polygon, marker, map));
            }


        }
        map.setOnMapClickListener(ml);
    }


}
