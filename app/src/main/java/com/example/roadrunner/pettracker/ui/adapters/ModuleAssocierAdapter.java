package com.example.roadrunner.pettracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Module;
import com.example.roadrunner.pettracker.model.Zone;
import com.example.roadrunner.pettracker.utils.DatabaseHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by gnut3ll4 on 01/11/15.
 */
public class ModuleAssocierAdapter extends BaseAdapter {

    private Zone zone;
    private Context context;
    private ArrayList<Module> modules;
    private Dao<Zone, ?> zoneDao;
    private Dao<Module, ?> moduleDao;

    public ModuleAssocierAdapter(Context context, ArrayList<Module> modules, Zone zone) {
        this.context = context;
        this.modules = modules;
        this.zone = zone;

        DatabaseHelper dbh = new DatabaseHelper(context);
        try {
            moduleDao = dbh.getDao(Module.class);
            zoneDao = dbh.getDao(Zone.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ModuleAssocierAdapter(Context context, Zone zone) {
        this.context = context;
        this.modules = new ArrayList<Module>();
        this.zone = zone;

        DatabaseHelper dbh = new DatabaseHelper(context);
        try {
            moduleDao = dbh.getDao(Module.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return modules.size();
    }

    @Override
    public Object getItem(int position) {
        return modules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return modules.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.row_module_associer, parent, false);
            ViewHolderModule holder = new ViewHolderModule();
            holder.checkBoxModuleAssocier = (CheckBox) convertView.findViewById(R.id.checkbox_module_associer);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_module_name);
            convertView.setTag(holder);

        }

        final Module module = (Module) getItem(position);

        ViewHolderModule holder = (ViewHolderModule) convertView.getTag();

        for (Module associatedModule : zone.getAssociatedModules()) {
            if (module == associatedModule) {
                holder.checkBoxModuleAssocier.setChecked(true);
            }
        }

        holder.checkBoxModuleAssocier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                module.setCurrentZone(isChecked ? zone : null);

                try {
                    moduleDao.update(module);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        holder.tvName.setText(module.getName());

        return convertView;
    }


    class ViewHolderModule {
        TextView tvName;
        CheckBox checkBoxModuleAssocier;
    }


}
