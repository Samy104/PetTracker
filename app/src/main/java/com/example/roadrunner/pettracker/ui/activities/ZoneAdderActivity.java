package com.example.roadrunner.pettracker.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Coordonnees;
import com.example.roadrunner.pettracker.model.Zone;
import com.example.roadrunner.pettracker.utils.DatabaseHelper;
import com.example.roadrunner.pettracker.utils.GrahamScan;
import com.example.roadrunner.pettracker.utils.MapsHelper;
import com.example.roadrunner.pettracker.utils.Point2D;
import com.example.roadrunner.pettracker.utils.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class ZoneAdderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MapView mapView;
    private GoogleMap googleMap;
    private ArrayList<Coordonnees> points;
    private DatabaseHelper databaseHelper;
    private Dao<Zone, ?> zoneDao;
    private Dao<Coordonnees, ?> coordonneesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_adder);
        points = new ArrayList<>();

        databaseHelper = new DatabaseHelper(ZoneAdderActivity.this);
        try {
            zoneDao = databaseHelper.getDao(Zone.class);
            coordonneesDao = databaseHelper.getDao(Coordonnees.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_confirm_add_zone);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Zone zone = new Zone("Test", true, points);
                    for (Coordonnees coordonnees : points) {
                        coordonnees.setZone(zone);
                    }

                    for (Coordonnees coordonnees : points) {
                        coordonneesDao.createOrUpdate(coordonnees);
                    }

                    zoneDao.create(zone);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mapView = (MapView) findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);

        if (mapView != null) {
            googleMap = mapView.getMap();

            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            MapsInitializer.initialize(this);

            LatLng coordinate = new LatLng(45.494441, -73.560595);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    googleMap.clear();
                    points.add(new Coordonnees(latLng));

                    floatingActionButton.setVisibility(points.size() > 1 ? View.VISIBLE : View.GONE);

                    if (points.size() == 2) {

                        double distance = MapsHelper.getRadius(points.get(0), points.get(1));

                        Circle circle = googleMap.addCircle(new CircleOptions()
                                .center(points.get(0).getLatLng())
                                .radius(distance)
                                .strokeColor(Color.BLACK)
                                .fillColor(Utils.stringToColour("Temp")));

                    } else if (points.size() > 2) {

                        Point2D[] points2D = new Point2D[points.size()];
                        for (int i = 0; i < points.size(); i++) {
                            points2D[i] = new Point2D(points.get(i).getLatitude(), points.get(i).getLongitude());
                        }

                        GrahamScan grahamScan = new GrahamScan(points2D);

                        points.clear();
                        for (Point2D point2D : grahamScan.hull()) {
                            points.add(new Coordonnees(point2D.x(), point2D.y()));
                        }

                        PolygonOptions polygon = new PolygonOptions();
                        for (Coordonnees coord : points) {
                            polygon.add(coord.getLatLng());
                        }
                        polygon.fillColor(Utils.stringToColour("Temp"));

                        googleMap.addPolygon(polygon);

                    }

//                    Toast.makeText(ZoneAdderActivity.this, "latLng:" + latLng, Toast.LENGTH_SHORT).show();
                }
            });


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zone_adder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        mapView.onResume();

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        mapView.onLowMemory();
    }

}
