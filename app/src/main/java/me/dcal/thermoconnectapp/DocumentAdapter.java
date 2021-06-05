package me.dcal.thermoconnectapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.dcal.thermoconnectapp.Modeles.TerraListData;

public class DocumentAdapter<T> extends ArrayAdapter<String> {

    private int resourceLayout;
    private Context mContext;

    public DocumentAdapter(@NonNull @NotNull Context context, int resource) {
        super(context, resource);
        this.resourceLayout = resource;
        this.mContext = context;
    }
    public void add(@Nullable String tld) {
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

        String tld = getItem(position);

        if (tld != null) {
            Button delete = (Button) v.findViewById(R.id.deletefile);
            TextView doc = (TextView) v.findViewById(R.id.docname);
            doc.setText(tld);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        return v;
    }
    public int getResourceLayout() {
        return resourceLayout;
    }

    public void setResourceLayout(int resourceLayout) {
        this.resourceLayout = resourceLayout;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
