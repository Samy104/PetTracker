package com.example.roadrunner.pettracker.ui.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.model.Zone;
import com.example.roadrunner.pettracker.ui.activities.ModuleAssociaterActivity;
import com.example.roadrunner.pettracker.ui.activities.ZoneAdderActivity;
import com.example.roadrunner.pettracker.utils.DatabaseHelper;
import com.example.roadrunner.pettracker.utils.MapsHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ZoneManagerFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private MapView mapView;
    private GoogleMap googleMap;
    private Dao<Zone, ?> zoneDao;
    private ArrayList<Zone> zones;
    private Dao<Module, ?> moduleDao;
    private HashMap<String, Marker> markerHashMap = new HashMap<>();

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
            googleMap.getUiSettings().setZoomControlsEnabled(false);


            LatLng coordinate = new LatLng(45.494441, -73.560595);
            MapsInitializer.initialize(getActivity());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));


            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLngClicked) {
                    for (Zone zone : zones) {
                        if (MapsHelper.isLatLngInZone(latLngClicked, zone)) {
                            Marker marker = markerHashMap.get(zone.getName());
                            marker.setPosition(latLngClicked);
                            marker.showInfoWindow();


                        }
                    }
                }
            });

            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLngClicked) {
                    for (final Zone zone : zones) {
                        if (MapsHelper.isLatLngInZone(latLngClicked, zone)) {
                            final Dialog dialog = new Dialog(getActivity());

                            //setting custom layout to dialog
                            dialog.setContentView(R.layout.dialog_zone_manager_menu);
                            dialog.setTitle(getString(R.string.choisir_action));

                            Button associerModuleButton = (Button) dialog.findViewById(R.id.btn_associer_module);
                            Button supprimerZoneButton = (Button) dialog.findViewById(R.id.btn_supprimer_zone);
                            Button toggleZoneButton = (Button) dialog.findViewById(R.id.btn_activer_zone);

                            if (zone.isActivated()) {
                                toggleZoneButton.setText(getString(R.string.desactiver_zone));
                            } else {
                                toggleZoneButton.setText(getString(R.string.activer_zone));
                            }

                            associerModuleButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(getActivity(), ModuleAssociaterActivity.class);
                                    intent.putExtra("zoneID",zone.getId());
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });

                            supprimerZoneButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    try {
                                        zoneDao.delete(zone);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                    refreshFragment();
                                }
                            });

                            toggleZoneButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    zone.setActivated(!zone.isActivated());
                                    try {
                                        zoneDao.update(zone);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    dialog.dismiss();
                                    refreshFragment();
                                }
                            });

                            dialog.show();


                        }
                    }
                }
            });


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


        try {
            zoneDao = databaseHelper.getDao(Zone.class);
            zones = new ArrayList<>(zoneDao.queryForAll());
            MapsHelper.displayZones(googleMap, zones);
            moduleDao = databaseHelper.getDao(Module.class);

            for (Zone zone : zones) {

                String titles = "";

                int i = 0;

                for (Module module : zone.getAssociatedModules()) {
                    if (i == zone.getAssociatedModules().size() - 1) {
                        titles += module.getName();
                    } else {
                        titles += module.getName() + "\n";
                    }
                    i++;
                }

                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(45.494441, -73.560595)) //random position
                        .title(zone.getName())
                        .snippet(titles));

                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.one_px_transparent));

                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        LinearLayout info = new LinearLayout(getActivity());
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(getActivity());
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(getActivity());
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);

                        return info;
                    }
                });

                markerHashMap.put(zone.getName(), marker);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


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

    private void refreshFragment() {
        Fragment frg = null;
        frg = getActivity().getFragmentManager().findFragmentByTag(ZoneManagerFragment.class.getName());
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }


}
