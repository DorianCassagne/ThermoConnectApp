package me.dcal.thermoconnectapp;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.TreeMap;

import me.dcal.thermoconnectapp.Modeles.BodyTerrarium;
import me.dcal.thermoconnectapp.Modeles.BodyTerrariumData;

public class TerraListAdapter extends ArrayAdapter<BodyTerrariumData>{

    private int resourceLayout;
    private Context mContext;

    public TerraListAdapter( Context context, int resource) {
        super(context, resource);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public void add(@Nullable BodyTerrariumData bd) {
        super.add(bd);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        BodyTerrariumData bd = getItem(position);

        if (bd != null) {
            TextView name = (TextView) v.findViewById(R.id.ListeNom);
            TextView temperature = (TextView) v.findViewById(R.id.ListeTemperature);
            TextView humidite = (TextView) v.findViewById(R.id.ListHumidite);

            if (name != null) {
                name.setText(bd.getId());
            }

            if (temperature != null) {
                temperature.setText(bd.getTemperature().toString());
            }

            if (humidite != null) {
                humidite.setText(bd.getHumidity().toString());
            }
        }

        return v;
    }
}
