package com.example.roadrunner.pettracker.ui.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Module;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;

import com.example.roadrunner.pettracker.utils.DatabaseHelper;

/**
 * Created by gnut3ll4 on 14/10/15.
 */
public class ModuleManagerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Module> modules;
    private Dao<Module, ?> moduleDao;

    public ModuleManagerAdapter(Context context, ArrayList<Module> modules) {
        this.context = context;
        this.modules = modules;

        DatabaseHelper dbh = new DatabaseHelper(context);
        try {
            moduleDao = dbh.getDao(Module.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ModuleManagerAdapter(Context context) {
        this.context = context;
        this.modules = new ArrayList<Module>();

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

    public void populateModuleList() {
        DatabaseHelper dbh = new DatabaseHelper(context);
        try {
            for (Module module : moduleDao.queryForAll()) {
                modules.add(module);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();
    }

    public void addModule(Module module) {
        DatabaseHelper dbh = new DatabaseHelper(context);
        try {
            moduleDao.create(module);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        modules.add(module);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.row_module, parent, false);
            ViewHolderModule holder = new ViewHolderModule();
            holder.btnDelete = (Button) convertView.findViewById(R.id.btn_module_delete);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_module_name);
            convertView.setTag(holder);

        }

        final Module module = (Module) getItem(position);

        ViewHolderModule holder = (ViewHolderModule) convertView.getTag();
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modules.remove(module);
                DatabaseHelper dbh = new DatabaseHelper(context);
                try {
                    moduleDao.delete(module);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                notifyDataSetChanged();
            }
        });

        holder.tvName.setText(module.getName());


        return convertView;
    }


    class ViewHolderModule {
        TextView tvName;
        Button btnDelete;
    }
}
