package com.example.roadrunner.pettracker.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.model.Zone;
import com.example.roadrunner.pettracker.ui.adapters.ModuleAssocierAdapter;
import com.example.roadrunner.pettracker.utils.DatabaseHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModuleAssociaterActivity extends AppCompatActivity {

    final Context context = this;
    private Toolbar toolbar;
    private ListView listView;
    private Zone currentZone;
    private ArrayList<Module> modules = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private Dao<Zone, ?> zoneDao;
    private int zoneID = 0;
    private Dao<Module, ?> moduleDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_associer);
        zoneID = getIntent().getIntExtra("zoneID", 0);

        databaseHelper = new DatabaseHelper(this);
        try {
            zoneDao = databaseHelper.getDao(Zone.class);
            currentZone = zoneDao.queryForEq("id", zoneID).get(0);
            modules.addAll(currentZone.getAssociatedModules());
            moduleDao = databaseHelper.getDao(Module.class);

            QueryBuilder<Module, ?> qb = moduleDao.queryBuilder();
            Where where = qb.where();
            where.isNull("zone_id");
            PreparedQuery<Module> prepare = qb.prepare();

            List<Module> notAssociatedModules = moduleDao.query(prepare);

            this.modules.addAll(notAssociatedModules);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.listview_modules);

        ModuleAssocierAdapter moduleAssocierAdapter = new ModuleAssocierAdapter(this, modules, currentZone);
        listView.setAdapter(moduleAssocierAdapter);
        moduleAssocierAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_module_associer, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
