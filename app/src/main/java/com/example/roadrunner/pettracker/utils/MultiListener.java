package com.example.roadrunner.pettracker.utils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MultiListener implements GoogleMap.OnMapClickListener
{
    List<GoogleMap.OnMapClickListener> mListeners = new ArrayList<GoogleMap.OnMapClickListener>();

    public void onMapClick(LatLng latLng)
    {
        for (GoogleMap.OnMapClickListener ompc : mListeners)
            ompc.onMapClick(latLng);
    }

    public void add(GoogleMap.OnMapClickListener omc) {
        mListeners.add(omc);
    }
}