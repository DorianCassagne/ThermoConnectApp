package me.dcal.thermoconnectapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

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
            TextView name = (TextView) v.findViewById(R.id.docname);
            TextView temperature = (TextView) v.findViewById(R.id.date);
            temperature.setBackgroundResource(R.drawable.rounded_corner);
            TextView humidite = (TextView) v.findViewById(R.id.ListHumidite);
            humidite.setBackgroundResource(R.drawable.rounded_corner);

            if (name != null) {
                name.setText(tld.getBt().getNameTerrarium());
            }

            if (temperature != null) {
                if(tld.getBd() != null) {
                    temperature.setText("Temperature:" + (double) Math.round(tld.getBd().getTemperature()*10)/10 + "°C");
                    if(tld.getBd().getTemperature()>tld.getBt().getTemperatureMax() || tld.getBd().getTemperature()<tld.getBt().getTemperatureMin()){
                        temperature.setBackgroundColor(Color.RED);
                        temperature.setTextColor(Color.WHITE);
                    }else{
                        temperature.setBackgroundColor(Color.parseColor("#7DCEA0"));
                        temperature.setTextColor(Color.WHITE);
                    }
                }else{
                    temperature.setText("Temperature: -");
                    temperature.setBackgroundColor(Color.LTGRAY);
                    temperature.setTextColor(Color.WHITE);
                }

            }

            if (humidite != null) {
                if(tld.getBd() != null) {
                    humidite.setText("Humidite:" + Math.round(tld.getBd().getHumidity()) + "%");
                    if(tld.getBd().getHumidity()>tld.getBt().getHumidityTerrarium() || tld.getBd().getHumidity()<tld.getBt().getHumidityTerrarium()){
                        humidite.setBackgroundColor(Color.RED);
                        humidite.setTextColor(Color.WHITE);
                    }else{
                        humidite.setBackgroundColor(Color.parseColor("#7DCEA0"));
                        humidite.setTextColor(Color.WHITE);
                    }
                }else{
                    humidite.setText("Humidite: -");
                    humidite.setBackgroundColor(Color.LTGRAY);
                    humidite.setTextColor(Color.WHITE);
                }
            }
        }

        return v;
    }
}
