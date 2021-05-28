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
import java.util.Map;
import java.util.TreeMap;

import me.dcal.thermoconnectapp.Modeles.BodyTerrarium;
import me.dcal.thermoconnectapp.Modeles.BodyTerrariumData;
import me.dcal.thermoconnectapp.Modeles.TerraListData;

public class TerraListAdapter<T> extends ArrayAdapter<TerraListData>{

    private int resourceLayout;
    private Context mContext;

    public TerraListAdapter( Context context, int resource) {
        super(context, resource);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public void add(@Nullable TerraListData tld) {
        super.add(tld);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        TerraListData tld = getItem(position);

        if (tld != null) {
            TextView name = (TextView) v.findViewById(R.id.ListeNom);
            TextView temperature = (TextView) v.findViewById(R.id.ListeTemperature);
            TextView humidite = (TextView) v.findViewById(R.id.ListHumidite);

            if (name != null) {
                name.setText(tld.getBt().getNameTerrarium());
            }

            if (temperature != null) {
                temperature.setText(tld.getBd().getTemperature().toString());
            }

            if (humidite != null) {
                humidite.setText(tld.getBd().getHumidity().toString());
            }
        }

        return v;
    }
}
