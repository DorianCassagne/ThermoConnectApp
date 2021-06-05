package me.dcal.thermoconnectapp;

import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.dcal.thermoconnectapp.Services.API;

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

    public String getData(){
        String data = "";
        for (AdaptaterRessource res :adapterressource){
            if (res.getEditalim().getText().length()>0 && !(res.getAlimentation().getText().equals(res.getEditalim().getText()))){
                res.getAlimentation().setText(res.getEditalim().getText());
                res.getAlimentation().setVisibility(View.VISIBLE);
                res.getEditalim().setVisibility(View.GONE);

                data = res.getDatealim().getText()+"|"+res.getAlimentation().getText()+";" ;

            }
        }
        return data;
    }

    @Override
    public void add(@Nullable String tld) {
        for (AdaptaterRessource res :adapterressource){
            if (res.getEditalim().getText().length()>0 && !(res.getAlimentation().getText().equals(res.getEditalim().getText()))){
                res.getAlimentation().setText(res.getEditalim().getText());
                res.getAlimentation().setVisibility(View.VISIBLE);
                res.getEditalim().setVisibility(View.GONE);

                String data = res.getDatealim().getText()+"|"+res.getAlimentation().getText() ;

                mContext.setAlimentationRow(data.length() >2 ? data : "" );

            }
        }
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
        pos = position;
        String tld = getItem(position);
        String[] data = new String[2];
        data = tld.split("\\|");
        String date;
        String alimstr;
        date = data[0];

        if (data.length>1){
            alimstr = data[1];
        }else{
            alimstr = null;
        }



        if (tld != null || tld != "") {
            TextView datealim = (TextView) v.findViewById(R.id.date);
            TextView alimentation = (TextView) v.findViewById(R.id.alimentation);
            TextView editalim = (TextView) v.findViewById(R.id.editalim);
            Button deletealim = (Button) v.findViewById(R.id.deletealim);

            adapterressource.add(new AdaptaterRessource(position, datealim, alimentation, editalim));
            deletealim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String data = datealim.getText()+"|"+alimentation.getText();
                    mContext.removeAlimentationRow(data);
                    remove(tld);
                }
            });
            datealim.setText(date);
            if ( alimstr != null){
                alimentation.setText(alimstr);
            }else{
                editalim.setVisibility(View.VISIBLE);
            }

            alimentation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editalim.setText(alimentation.getText());
                    alimentation.setVisibility(View.GONE);
                    editalim.setVisibility(View.VISIBLE);
                }
            });


            KeyboardVisibilityEvent.setEventListener( mContext, new KeyboardVisibilityEventListener() {
                @Override
                public void onVisibilityChanged(boolean b) {
                    if (editalim.getVisibility()==View.VISIBLE && !isKeyboardShown(editalim.getRootView())) {
                        alimentation.setText(editalim.getText());
                        alimentation.setVisibility(View.VISIBLE);
                        editalim.setVisibility(View.GONE);

                        String data = datealim.getText()+"|"+alimentation.getText() ;

                        //mContext.setAlimentationRow(data.length() >2 ? data : "" );
                    }
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
                                    String date = year+"-"+  ((month<=9) ? Integer.parseInt("0"+month) : month) + "-" + dayOfMonth;
                                    datealim.setText(date);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                }
            });

        }

        return v;
    }

    public void calendar(){

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
