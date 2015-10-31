package com.example.roadrunner.pettracker;

import android.test.InstrumentationTestCase;

import com.example.roadrunner.pettracker.model.Coordonnees;
import com.example.roadrunner.pettracker.utils.MapsHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by gnut3ll4 on 31/10/15.
 */
public class GeometryTest extends InstrumentationTestCase {

    private ArrayList<Coordonnees> circle;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        circle = new ArrayList<>();
        circle.add(new Coordonnees(45.493684, -73.562085));
        circle.add(new Coordonnees(45.494270, -73.563732));
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    public void testIsInCircle() {
        LatLng pointInCircle = new LatLng(45.494090, -73.562954);

        assertTrue(MapsHelper.isPointInCircle(
                        circle.get(0).getLatLng(),
                        MapsHelper.getDistanceBetween(circle.get(0), circle.get(1)),
                        pointInCircle
                )
        );
    }

    public void testIsNotInCircle() {
        LatLng pointNotInCircle = new LatLng(45.493924, -73.567122);

        assertFalse(MapsHelper.isPointInCircle(
                        circle.get(0).getLatLng(),
                        MapsHelper.getDistanceBetween(circle.get(0), circle.get(1)),
                        pointNotInCircle
                )
        );

    }

    public void testDistanceBetween() {
        Coordonnees coordonnees1 = new Coordonnees(0, 0);
        Coordonnees coordonnees2 = new Coordonnees(0, 1);

        assertTrue(MapsHelper.getDistanceBetween(coordonnees1, coordonnees2) == 1.0f);
    }

}
