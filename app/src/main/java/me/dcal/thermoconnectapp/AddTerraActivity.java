package me.dcal.thermoconnectapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import me.dcal.thermoconnectapp.Modeles.BodyTerrarium;
import me.dcal.thermoconnectapp.Services.API;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTerraActivity extends AppCompatActivity {
    TextView timeMin;
    TextView timeMax;
    TimePickerDialog timeMinPickerDialog;
    TimePickerDialog timeMaxPickerDialog;
    TextView textChaud;
    TextView textFroid;
    TextView textHumidity;
    EditText nameTerrarium;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_terra);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTailleAdapter();
        timeMin=findViewById(R.id.timeMin);
        timeMax=findViewById(R.id.timeMax);
        textChaud=findViewById(R.id.textChaud);
        textFroid=findViewById(R.id.textFroid);
        textHumidity=findViewById(R.id.textHumidity);
        nameTerrarium=findViewById(R.id.nameTerrarium);
        /*timeMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }
    public void setTailleAdapter(){String[] arraySpinner = new String[] {
            "1", "2", "3", "4", "5", "6", "7"
    };
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.deconnexion:
                API.setBodyConnexion(getApplicationContext(),null);
                Intent i=new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement

    }

    public void clickTimeMin(View v){
        API.launchShortToast(getApplicationContext(),"onClick");
        timeMinPickerDialog=new TimePickerDialog(AddTerraActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                if(minutes<=9){
                    timeMin.setText(hour+":0"+minutes);
                }else{
                    timeMin.setText(hour+":"+minutes);
                }
            }
        },Integer.parseInt(timeMin.getText().toString().split(":")[0]),Integer.parseInt(timeMin.getText().toString().split(":")[1]),true);
        timeMinPickerDialog.show();
    }
    public void clickTimeMax(View v){
        API.launchShortToast(getApplicationContext(),"onClick");
        timeMaxPickerDialog=new TimePickerDialog(AddTerraActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                if(minutes<=9){
                    timeMax.setText(hour+":0"+minutes);
                }else{
                    timeMax.setText(hour+":"+minutes);
                }

            }
        },Integer.parseInt(timeMax.getText().toString().split(":")[0]),Integer.parseInt(timeMax.getText().toString().split(":")[1]),true);
        timeMaxPickerDialog.show();
    }
    public void plusPointChaud(View v){
        textChaud.setText((Integer.parseInt(textChaud.getText().toString())+1)+"");
    }
    public void moinsPointChaud(View v){
        textChaud.setText((Integer.parseInt(textChaud.getText().toString())-1)+"");
    }
    public void moinsPointFroid(View v){
        textFroid.setText((Integer.parseInt(textFroid.getText().toString())-1)+"");
    }
    public void plusPointFroid(View v){
        textFroid.setText((Integer.parseInt(textFroid.getText().toString())+1)+"");
    }
    public void plusHumidity(View v){
        int humidity =(Integer.parseInt(textHumidity.getText().toString())+1);
        if(humidity<=100) {
            textHumidity.setText((Integer.parseInt(textHumidity.getText().toString()) + 1) + "");
        }
    }
    public void moinsHumidity(View v){
        int humidity=(Integer.parseInt(textHumidity.getText().toString())-1);
        if(humidity>=0){
            textHumidity.setText(humidity+"");
        }
    }
    public void ajouter(View v){
        if(nameTerrarium.getText().toString()!="") {
            BodyTerrarium bodyTerrarium = new BodyTerrarium(API.getBodyConnexion(getApplicationContext()),
                    nameTerrarium.getText().toString(),
                    spinner.getSelectedItem().toString(),
                    timeMin.getText().toString() + ":00",
                    timeMax.getText().toString() + ":00",
                    Double.parseDouble(textChaud.getText().toString()),
                    Double.parseDouble(textFroid.getText().toString()),
                    Double.parseDouble(textHumidity.getText().toString()));
            Call<Integer> reponse= API.getInstance().simpleService.ajoutTerrarium(bodyTerrarium);
            reponse.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Integer i=response.body();
                    Toast toast = Toast.makeText(getApplicationContext(), i+"", Toast.LENGTH_SHORT);
                    toast.show();
                    /*if(true==i.booleanValue()){
                        API.setBodyConnexion(getApplicationContext(),body);
                        Intent intent = new Intent(getApplicationContext(), TerraListActivity.class);
                        startActivity(intent);
                    }*/
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    call.request().url();
                    Toast toast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        }
    }
}