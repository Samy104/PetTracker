package com.example.roadrunner.pettracker.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.ui.adapters.ModuleManagerAdapter;

import java.util.ArrayList;


public class ModuleManagerFragment extends Fragment {

    public static ModuleManagerFragment newInstance(String param1, String param2) {
        ModuleManagerFragment fragment = new ModuleManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ModuleManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    private FloatingActionButton floatingActionButtonModuleAdd;
    private ModuleManagerAdapter moduleManagerAdapter;
    private ListView listViewModules;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_module_manager, container, false);

        listViewModules = (ListView) inflate.findViewById(R.id.listview_modules);

        floatingActionButtonModuleAdd = (FloatingActionButton) inflate.findViewById(R.id.fab_add_module);
        floatingActionButtonModuleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Bouton + pressé",Toast.LENGTH_SHORT).show();
            }
        });

        ArrayList<Module> modules = new ArrayList<>();
        modules.add(new Module("Rex"));
        modules.add(new Module("Balto"));
        modules.add(new Module("Chien"));

        moduleManagerAdapter = new ModuleManagerAdapter(getActivity(),modules);

        listViewModules.setAdapter(moduleManagerAdapter);

        moduleManagerAdapter.notifyDataSetChanged();

        return inflate;


    }

}
