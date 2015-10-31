package com.example.roadrunner.pettracker.ui.fragments;

import android.app.Fragment;

import android.os.Bundle;


import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Coordonnees;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.model.Zone;
import com.example.roadrunner.pettracker.utils.DatabaseHelper;
import com.example.roadrunner.pettracker.utils.MapsHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.dao.Dao;
import com.sromku.polygon.Point;
import com.sromku.polygon.Polygon;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class MapsFragment extends Fragment {

    MapView mapView;
    DatabaseHelper databaseHelper;
    GoogleMap googleMap;
    private Dao<Module, ?> moduleDao;
    private ArrayList<Module> modules;
    HashMap<String, Marker> moduleMarkerHashMap = new HashMap<>();
    private Dao<Zone, ?> zoneDao;
    private ArrayList<Zone> zones;
    private Handler markerMovingHandler;
    private MarkerMovingRunnable markerMovingThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        databaseHelper = new DatabaseHelper(getActivity());
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        if (mapView != null) {
            googleMap = mapView.getMap();
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            LatLng coordinate = new LatLng(45.494441, -73.560595);
            MapsInitializer.initialize(getActivity());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));

            try {
                zoneDao = databaseHelper.getDao(Zone.class);
                zones = new ArrayList<>(zoneDao.queryForAll());
                MapsHelper.displayZones(googleMap, zones);
                moduleDao = databaseHelper.getDao(Module.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                modules = new ArrayList<>(moduleDao.queryForAll());

                for (Module module : modules) {
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(module.getLatLnt())
                            .title(module.getName()));

                    moduleMarkerHashMap.put(module.getName(), marker);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            markerMovingHandler = new android.os.Handler();

            markerMovingThread = new MarkerMovingRunnable() {
                @Override
                public void run() {
                    super.run();
                    markerMovingHandler.postDelayed(this, 2000);
                }
            };
            markerMovingHandler.postDelayed(markerMovingThread, 2000);

        }

        return view;

    }

    @Override
    public void onStop() {
        super.onStop();
        markerMovingHandler.removeCallbacks(markerMovingThread);
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

    public void notifyExitZone(Module module) {
        Zone zone = module.getCurrentZone();
        Toast.makeText(getActivity(), module.getName() + " a quitté la zone " + module.getCurrentZone().getName(), Toast.LENGTH_SHORT).show();
        Log.i("Exited zone", module.getName() + " a quitté la zone " + module.getCurrentZone().getName());
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        android.graphics.Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final LinearInterpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    abstract class MarkerMovingRunnable implements Runnable {

        @Override
        public void run() {
            for (Module module : modules) {

                ArrayList<Coordonnees> coordonnees = new ArrayList<>(module.getCurrentZone().getCoordonnees());

                if (coordonnees.size() < 2) {
                    continue;
                }

                boolean outOfZone = true;
                if (MapsHelper.isModuleInItsZone(module)) {
                    outOfZone = false;
                }
                module.generateNewPosition();
                Marker marker = moduleMarkerHashMap.get(module.getName());
                animateMarker(marker, module.getLatLnt(), false);
                moduleMarkerHashMap.put(module.getName(), marker);

                if (!MapsHelper.isModuleInItsZone(module) && !outOfZone) {
                    notifyExitZone(module);
                }

            }
        }
    }
}
