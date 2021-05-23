package me.dcal.thermoconnectapp;

import android.content.Intent;
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

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AnimalActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_animal);


        /*LineChart pieChart = (LineChart) findViewById(R.id.barchart);
        ArrayList NoOfEmp = new ArrayList();


        NoOfEmp.add(new Entry(945f, 450));
        NoOfEmp.add(new Entry(1040f, 510));
        NoOfEmp.add(new Entry(1133f, 520));
        NoOfEmp.add(new Entry(1240f, 600));
        NoOfEmp.add(new Entry(1369f, 615));
        NoOfEmp.add(new Entry(1487f, 689));
        NoOfEmp.add(new Entry(1501f, 750));

        LineDataSet dataSet = new LineDataSet(NoOfEmp, "Evolution du poids");

        ArrayList year = new ArrayList();

        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");
        LineData data = new LineData(dataSet);

        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);*/

    }

}