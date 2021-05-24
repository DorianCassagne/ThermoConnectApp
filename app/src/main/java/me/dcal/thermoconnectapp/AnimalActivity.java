package me.dcal.thermoconnectapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AnimalActivity extends AppCompatActivity {

    LineChart pieChart;
    TextView newWeight;
    List<Uri> UriTabDoc;
    Uri finalimage;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        pieChart = (LineChart) findViewById(R.id.barchart);
        newWeight = (TextView) findViewById(R.id.nouveaupoids);
        ArrayList NoOfEmp = new ArrayList();


        NoOfEmp.add(new Entry(945f, 450));
        NoOfEmp.add(new Entry(1040f, 510));
        NoOfEmp.add(new Entry(1133f, 520));
        NoOfEmp.add(new Entry(1240f, 600));
        NoOfEmp.add(new Entry(1369f, 615));
        NoOfEmp.add(new Entry(1487f, 689));
        NoOfEmp.add(new Entry(1501f, 750));

        LineDataSet dataSet = new LineDataSet(NoOfEmp, "Evolution du poids");

        LineData data = new LineData(dataSet);

        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);


        Button buttonLoadFile = (Button) findViewById(R.id.addfiles);
        buttonLoadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FilePicker Fp = new FilePicker();
                Fp.requestPermission(UriTabDoc, finalimage);
            }
        });
    }

    public void addWeight(View v){

    }



}