package com.example.roadrunner.pettracker.utils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre on 30/10/2015.
 */
public class MarkersOnClickListenerPolygon implements GoogleMap.OnMapClickListener{

    private PolygonOptions p;
    private Marker marker;
    private GoogleMap map;
    public MarkersOnClickListenerPolygon(PolygonOptions p, Marker marker, GoogleMap map) {
        System.out.println("yo");
        this.p = p;
        this.marker = marker;
        this.map = map;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        System.out.println(p.getPoints().toString());
        if(isPointInPolygon(latLng, p.getPoints())) {
            System.out.println("Dans le polygone");

                double[] centroid = { 0.0, 0.0 };

                for (int i = 0; i < p.getPoints().size(); i++) {
                    centroid[0] += p.getPoints().get(i).latitude;
                    centroid[1] += p.getPoints().get(i).longitude;
                }

                int totalPoints = p.getPoints().size();
                centroid[0] = centroid[0] / totalPoints;
                centroid[1] = centroid[1] / totalPoints;
                LatLng center = new LatLng(centroid[0], centroid[1]);
                 marker = map.addMarker(new MarkerOptions()
                            .position(center)
                            .title("Zone polygonale " + p.toString())
                            .snippet("Cette zone est associée au collier : ")
            );
            marker.showInfoWindow();
        }
        else if (marker !=null) marker.setVisible(false);
    }

    private boolean isPointInPolygon(LatLng tap, List<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }


}
