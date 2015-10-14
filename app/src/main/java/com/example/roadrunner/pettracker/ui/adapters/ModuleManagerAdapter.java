package com.example.roadrunner.pettracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roadrunner.pettracker.R;
import com.example.roadrunner.pettracker.model.Module;

import java.util.ArrayList;

/**
 * Created by gnut3ll4 on 14/10/15.
 */
public class ModuleManagerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Module> modules;


    public ModuleManagerAdapter(Context context, ArrayList<Module> modules) {
        this.context = context;
        this.modules = modules;
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

    public void addModule(Module module) {
        modules.add(module);
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
                Toast.makeText(context, "Suppression demandée", Toast.LENGTH_SHORT).show();
                modules.remove(module);
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
