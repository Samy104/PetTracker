package com.example.roadrunner.pettracker;

import android.test.InstrumentationTestCase;

import com.example.roadrunner.pettracker.model.Coordonnees;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.model.Zone;
import com.example.roadrunner.pettracker.utils.DatabaseHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by gnut3ll4 on 14/10/15.
 */
public class DatabaseTest extends InstrumentationTestCase {

    DatabaseHelper databaseHelper;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        databaseHelper = new DatabaseHelper(getInstrumentation().getTargetContext());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


    public void testDatabase() {


        ArrayList<Module> modules = new ArrayList<>();
        modules.add(new Module("Rex"));
        modules.add(new Module("Balto"));

        ArrayList<Coordonnees> coordonnees = new ArrayList<>();
        coordonnees.add(new Coordonnees(45.4944449, -73.5611256));
        coordonnees.add(new Coordonnees(45.4951989, -73.5625794));

        Zone zone = new Zone("test zone", true, coordonnees);
        modules.get(0).setCurrentZone(zone);
        modules.get(1).setCurrentZone(zone);
        coordonnees.get(0).setZone(zone);
        coordonnees.get(1).setZone(zone);

        try {


            Dao<Zone, ?> zoneDao = databaseHelper.getDao(Zone.class);
            Dao<Coordonnees, ?> coordonneesDao = databaseHelper.getDao(Coordonnees.class);
            Dao<Module, ?> moduleDao = databaseHelper.getDao(Module.class);

            zoneDao.deleteBuilder().delete();
            moduleDao.deleteBuilder().delete();
            coordonneesDao.deleteBuilder().delete();

            for (Module module : modules) {
                zone.addModule(module);
                moduleDao.createOrUpdate(module);
            }

            for (Coordonnees coordonnees1 : coordonnees) {
                coordonneesDao.createOrUpdate(coordonnees1);
            }

            zoneDao.createOrUpdate(zone);


            assertEquals(2, moduleDao.queryForAll().size());
            assertEquals(2, coordonneesDao.queryForAll().size());

            zoneDao.refresh(zone);

            assertEquals(2, zoneDao.queryForAll().get(0).getAssociatedModules().size());
            assertEquals(2, zoneDao.queryForAll().get(0).getCoordonnees().size());


            assertNotNull(moduleDao.queryForAll().get(0));

            zoneDao.deleteBuilder().delete();
            moduleDao.deleteBuilder().delete();
            coordonneesDao.deleteBuilder().delete();

        } catch (SQLException e) {
            assertTrue(e.getMessage(), false);
            e.printStackTrace();
        }

    }

}