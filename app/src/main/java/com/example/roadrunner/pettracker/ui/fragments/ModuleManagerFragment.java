package com.example.roadrunner.pettracker.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
// Addition
import android.content.Context;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;



import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.ui.adapters.ModuleManagerAdapter;

import java.io.InputStream;
import java.util.ArrayList;


public class ModuleManagerFragment extends Fragment {

    private static final int PICK_IMAGE=1;

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
                // Dialog Layout view
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptView = li.inflate(R.layout.fragment_module_prompt, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                // Set the layout for the builder
                alertDialogBuilder.setView(promptView);

                final EditText input = (EditText) promptView.findViewById(R.id.moduleName);

                final Button imageUploadButton = (Button) promptView.findViewById(R.id.upld_img_btn);
                imageUploadButton.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");

                        Intent chooserIntent = Intent.createChooser(intent, "Select Image");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                        startActivityForResult(chooserIntent, PICK_IMAGE);
                    }
                });

                // Dialog Setup
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moduleManagerAdapter.addModule(new Module(input.getText().toString()));
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,	int id) {
                                        dialog.cancel();
                                    }
                                });

                // Dialog Creation
                AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();

                // Notify the adapter of the changes
                moduleManagerAdapter.notifyDataSetChanged();
            }
        });


        // Get the module adapter and populate the list from the DB
        moduleManagerAdapter = new ModuleManagerAdapter(getActivity());
        moduleManagerAdapter.populateModuleList();

        //Set the list view adapter
        listViewModules.setAdapter(moduleManagerAdapter);

        moduleManagerAdapter.notifyDataSetChanged();

        return inflate;


    }


}
