package me.dcal.thermoconnectapp;

import android.view.View;
import android.widget.TextView;

public class AdaptaterRessource {

    int position;
    TextView datealim;
    TextView alimentation;
    TextView editalim;
    View view;

    public AdaptaterRessource(View v, int position, TextView datealim, TextView alimentation, TextView editalim) {
        this.position = position;
        this.datealim = datealim;
        this.alimentation = alimentation;
        this.editalim = editalim;
        this.view = v;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public TextView getDatealim() {
        return datealim;
    }

    public void setDatealim(TextView datealim) {
        this.datealim = datealim;
    }

    public TextView getAlimentation() {
        return alimentation;
    }

    public void setAlimentation(TextView alimentation) {
        this.alimentation = alimentation;
    }

    public TextView getEditalim() {
        return editalim;
    }

    public void setEditalim(TextView editalim) {
        this.editalim = editalim;
    }
}
