package me.dcal.thermoconnectapp;

import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.dcal.thermoconnectapp.Services.API;

import static java.util.stream.Collectors.toList;

public class AlimentationAdapter <T> extends ArrayAdapter<String> {
    private int resourceLayout;
    private AlimentationActivity mContext;
    int pos;
    List<AdaptaterRessource> adapterressource = new ArrayList<>();
    public AlimentationAdapter(@NonNull AlimentationActivity context, int resource) {
        super(context, resource);
        this.resourceLayout = resource;
        this.mContext = context;

    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    public String getData(){
        String data = "";
        List<String> listdata = new ArrayList<>();
        for (AdaptaterRessource res :adapterressource){
            if (res.getAlimentation().getText().length()>0 || res.getEditalim().getText().length()>0 && !(res.getAlimentation().getText().equals(res.getEditalim().getText()))){
                TableLayout layout = (TableLayout) res.getView();

                if (((TextView) layout.findViewById(R.id.alimentation)).getVisibility()==View.GONE && ((TextView) layout.findViewById(R.id.editalim)).getVisibility()==View.VISIBLE){
                    res.getAlimentation().setText(res.getEditalim().getText());
                    res.getAlimentation().setVisibility(View.VISIBLE);
                    res.getEditalim().setVisibility(View.GONE);
                }
                String value = res.getDatealim().getText()+";"+res.getAlimentation().getText()+"|";
                if (!(listdata.contains(value)) && !(value.contains(";|")) && !(value.startsWith(";")))
                    listdata.add(value);


            }
        }
        for (String el : listdata)
            data += el;

        if (data.endsWith("|"))
            data = data.substring(0, data.length()-1);
        return data;
    }

    @Override
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
        String[] data = new String[2];
        data = tld.split(";");
        String date;
        String alimstr;
        date = data[0];

        if (data.length>1){
            alimstr = data[1];
        }else{
            alimstr = null;
        }

        List<Integer> arraypos = new ArrayList<Integer>();
        for (AdaptaterRessource res :adapterressource){
            arraypos.add(res.getPosition());

        }

        if ((tld != null || tld != "") ) {
            TextView datealim = (TextView) v.findViewById(R.id.date);
            TextView alimentation = (TextView) v.findViewById(R.id.alimentation);
            TextView editalim = (TextView) v.findViewById(R.id.editalim);
            Button deletealim = (Button) v.findViewById(R.id.deletealim);

            //if (!arraypos.contains(position)){
                adapterressource.add(new AdaptaterRessource(v, position, datealim, alimentation, editalim));
            //}

            deletealim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String data = datealim.getText()+";"+alimentation.getText();
                    if (data.endsWith(";")){
                        data = data.substring(0, data.length()-1);
                    }
                    List<AdaptaterRessource> deletevalue = new ArrayList<>();

                    for (AdaptaterRessource res :adapterressource){
                        if (res.getPosition()==position){
                            deletevalue.add(res);

                        }
                    }
                    for (AdaptaterRessource elem : deletevalue)
                        adapterressource.remove(elem);
                    mContext.removeAlimentationRow(data);
                    remove(tld);

                }
            });

            datealim.setText(date);
            if ( alimstr != null){
                alimentation.setText(alimstr);
                alimentation.setVisibility(View.VISIBLE);
                editalim.setVisibility(View.GONE);
            }else{
                alimentation.setVisibility(View.GONE);
                editalim.setVisibility(View.VISIBLE);
            }


            KeyboardVisibilityEvent.setEventListener( mContext, new KeyboardVisibilityEventListener() {
                @Override
                public void onVisibilityChanged(boolean b) {
                    if (editalim.getText().length()> 0 && editalim.getVisibility()==View.VISIBLE && !isKeyboardShown(editalim.getRootView())) {
                        alimentation.setText(editalim.getText());
                        alimentation.setVisibility(View.VISIBLE);
                        editalim.setVisibility(View.GONE);
                        mContext.save.setVisibility(View.VISIBLE);
                    }
                }
            });

            alimentation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editalim.setText(alimentation.getText());
                    alimentation.setVisibility(View.GONE);
                    editalim.setVisibility(View.VISIBLE);
                }
            });


            datealim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    API.launchShortToast(mContext,"onClick");
                    int mYear, mMonth, mDay, mHour, mMinute;
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    String date = year+"-"+  ((month<=9) ? "0"+month : month) + "-" + ((dayOfMonth<=9) ? "0"+dayOfMonth : dayOfMonth);
                                    datealim.setText(date);
                                    String value = getItem(position);
                                    String[] data = value.split(";");
                                    String newvalue = "";
                                    if (!date.equals(data[0])){
                                        if (data.length>1){
                                            newvalue = date +";"+data[1];
                                        }else{
                                            newvalue = date ;
                                        }
                                        mContext.save.setVisibility(View.VISIBLE);
                                        remove(value);
                                        add(newvalue);

                                    }

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                }
            });

        }

        return v;
    }


    private boolean isKeyboardShown(View rootView) {
        /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128;

        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        /* heightDiff = rootView height - status bar height (r.top) - visible frame height (r.bottom - r.top) */
        int heightDiff = rootView.getBottom() - r.bottom;
        /* Threshold size: dp to pixels, multiply with display density */
        boolean isKeyboardShown = heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density;

        return isKeyboardShown;
    }

}
