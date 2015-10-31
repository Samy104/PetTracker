package com.example.roadrunner.pettracker.utils;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Alexandre on 30/10/2015.
 */
public class MarkersOnClickListener implements GoogleMap.OnMapClickListener {

        private Circle circle;
        private GoogleMap map;
        private Marker marker;
        public MarkersOnClickListener (Circle circle, GoogleMap map, Marker marker){
            this.circle = circle;
            this.map = map;
            this.marker = marker;
        }

        public void onMapClick(LatLng latLng) {
            System.out.println("Click on the goddamn map !");

            Location a = new Location("Point click");
            Location b = new Location("Point centre zone");
            a.setLatitude(latLng.latitude);
            a.setLongitude(latLng.longitude);
            b.setLatitude(circle.getCenter().latitude);
            b.setLongitude(circle.getCenter().longitude);
            System.out.println("distance a-b" + a.distanceTo(b));
            System.out.println("Radius " + circle.getCenter().latitude);
            if (a.distanceTo(b) <= circle.getRadius()){
                marker = map.addMarker(new MarkerOptions()
                                .position(circle.getCenter())
                                .title("Zone circulaire no " + circle.getId())
                                .snippet("Cette zone est associée au collier : ")
                );
                marker.showInfoWindow();
            }
            else if (marker !=null) marker.setVisible(false);
        }
    }

