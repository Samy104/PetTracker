package com.example.roadrunner.pettracker.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.model.Zone;
import com.example.roadrunner.pettracker.ui.activities.ZoneAdderActivity;
import com.example.roadrunner.pettracker.utils.DatabaseHelper;
import com.example.roadrunner.pettracker.utils.MapsHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

public class ZoneManagerFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private MapView mapView;
    private GoogleMap googleMap;
    private Dao<Zone, ?> zoneDao;
    private ArrayList<Zone> zones;
    private Dao<Module, ?> moduleDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zone_manager, container, false);

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
        }


        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_add_zone);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ZoneAdderActivity.class);
                startActivity(intent);
            }
        });


        return view;
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
