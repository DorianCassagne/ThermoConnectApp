package me.dcal.thermoconnectapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.List;

import me.dcal.thermoconnectapp.Modeles.BodyAnimal;
import me.dcal.thermoconnectapp.Modeles.BodyTerrarium;
import me.dcal.thermoconnectapp.Services.API;
import me.dcal.thermoconnectapp.Services.BodyConnexion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TerrariumActivity extends AppCompatActivity {

    private BodyTerrarium bt;
    private TimePickerDialog timeMinPickerDialog;
    private TimePickerDialog timeMaxPickerDialog;
    private TextView TitleTerrarium;
    private EditText TitleTerrariumEdit;
    private TextView temperatureMin;
    private NumberPicker minPicker;
    private TextView temperatureMax;
    private NumberPicker maxPicker;
    private TextView humidite;
    private NumberPicker humiditePicker;
    private TextView heureMinTerrarium;
    private TextView heureMaxTerrarium;
    private Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bt = (BodyTerrarium) getIntent().getSerializableExtra("Terrarium");
        setContentView(R.layout.activity_terrarium);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BodyConnexion body=API.getBodyConnexion(getApplicationContext());
        Toast toast = Toast.makeText(getApplicationContext(), body.login , Toast.LENGTH_LONG);
        toast.show();

        //Init
        TitleTerrarium = (TextView)findViewById(R.id.TitleTerrarium);
        TitleTerrariumEdit = (EditText)findViewById(R.id.TitleTerrariumEdit);
        temperatureMin = (TextView)findViewById(R.id.TemperatureFroidTerrarium);
        minPicker = (NumberPicker)findViewById(R.id.TemperatureFroidTerrariumEdit);
        temperatureMax = (TextView)findViewById(R.id.TemperatureChaudTerrarium);
        maxPicker = (NumberPicker)findViewById(R.id.TemperatureChaudTerrariumEdit);
        humidite = (TextView)findViewById(R.id.HumiditéTerrarium);
        humiditePicker = (NumberPicker)findViewById(R.id.HumiditéTerrariumEdit);
        heureMinTerrarium = (TextView)findViewById(R.id.HeureMinTerrarium);
        heureMaxTerrarium = (TextView)findViewById(R.id.HeureMaxTerrarium);
        save_button = (Button)findViewById(R.id.save_button);
        //Setup Value
        TitleTerrariumEdit.setText(bt.getNameTerrarium());
        TitleTerrarium.setText(bt.getNameTerrarium());
        temperatureMin.setText(bt.getTemperatureMin().toString());
        minPicker.setMinValue(0);
        minPicker.setMaxValue(100);
        int minTemperatureValue = (int)Math.round(bt.getTemperatureMin());
        minPicker.setValue(minTemperatureValue);
        temperatureMax.setText(bt.getTemperatureMax().toString());
        maxPicker.setMaxValue(100);
        maxPicker.setMinValue(0);
        int maxTemperatureValue = (int)Math.round(bt.getTemperatureMax());
        maxPicker.setValue(maxTemperatureValue);
        humidite.setText(bt.getHumidityTerrarium().toString());
        humiditePicker.setMaxValue(100);
        humiditePicker.setMinValue(0);
        int humiditeValue = (int)Math.round(bt.getHumidityTerrarium());
        humiditePicker.setValue(humiditeValue);
        heureMinTerrarium.setText(bt.getStartLightTime());
        heureMaxTerrarium.setText(bt.getStopLightTime());
        bt.setBodyConnexion(body);
        Call<List<BodyAnimal>> list = API.getInstance().simpleService.listAnimal(bt);
        ListView AnimalList = (ListView)findViewById(R.id.AnimalList);
        ArrayAdapter<BodyAnimal> arrayAdapter = new ArrayAdapter<BodyAnimal>(this, android.R.layout.simple_list_item_1);
        AnimalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BodyAnimal ba = (BodyAnimal)parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), AnimalActivity.class);
                intent.putExtra("Animal", ba);
                startActivity(intent);
            }
        });
        list.enqueue(new Callback<List<BodyAnimal>>() {
            @Override
            public void onResponse(Call<List<BodyAnimal>> call, Response<List<BodyAnimal>> response) {
                for(BodyAnimal ba : response.body())
                    arrayAdapter.add(ba);
                AnimalList.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(Call<List<BodyAnimal>> call, Throwable t) {
                API.launchShortToast(getApplicationContext(), "KO");
            }
        });


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
            case R.id.add:
                Intent a=new Intent(this, AddAnimalActivity.class);
                startActivity(a);
                return true;
            case R.id.animal:
                Intent q =new Intent(this, AnimalActivity.class);
                startActivity(q);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement

    }

    public void ajouterAnimal(View v){
        Intent i=new Intent(this, AddAnimalActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Call<List<BodyAnimal>> list = API.getInstance().simpleService.listAnimal(bt);
        ListView AnimalList = (ListView)findViewById(R.id.AnimalList);
        ArrayAdapter<BodyAnimal> arrayAdapter = new ArrayAdapter<BodyAnimal>(this, android.R.layout.simple_list_item_1);

        AnimalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BodyAnimal ba = (BodyAnimal)parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), AnimalActivity.class);
                intent.putExtra("Animal", ba);
                startActivity(intent);
            }
        });
        list.enqueue(new Callback<List<BodyAnimal>>() {
            @Override
            public void onResponse(Call<List<BodyAnimal>> call, Response<List<BodyAnimal>> response) {
                for(BodyAnimal ba : response.body())
                    arrayAdapter.add(ba);
                AnimalList.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(Call<List<BodyAnimal>> call, Throwable t) {
                API.launchShortToast(getApplicationContext(), "KO");
            }
        });
    }

    //Modification des valeurs

    public void updateInfoVue(View v){
        TitleTerrarium.setVisibility(View.VISIBLE);
        TitleTerrariumEdit.setVisibility(View.GONE);
        TitleTerrarium.setText(TitleTerrariumEdit.getText().toString());
        temperatureMax.setVisibility(View.VISIBLE);
        maxPicker.setVisibility(View.GONE);
        temperatureMax.setText(maxPicker.getValue() + ".0");
        temperatureMin.setVisibility(View.VISIBLE);
        minPicker.setVisibility(View.GONE);
        temperatureMin.setText(minPicker.getValue() + ".0");
        humiditePicker.setVisibility(View.GONE);
        humidite.setVisibility(View.VISIBLE);
        humidite.setText(humiditePicker.getValue()+".0");
        verification();
    }

    public void modiftemperaturemin(View v){
        TitleTerrarium.setVisibility(View.VISIBLE);
        TitleTerrariumEdit.setVisibility(View.GONE);
        TitleTerrarium.setText(TitleTerrariumEdit.getText().toString());
        temperatureMax.setVisibility(View.VISIBLE);
        maxPicker.setVisibility(View.GONE);
        temperatureMax.setText(maxPicker.getValue() + ".0");
        temperatureMin.setVisibility(View.GONE);
        minPicker.setVisibility(View.VISIBLE);
        humiditePicker.setVisibility(View.GONE);
        humidite.setVisibility(View.VISIBLE);
        humidite.setText(humiditePicker.getValue()+".0");
        verification();

    }

    public void modiftemperaturemax(View v){
        TitleTerrarium.setVisibility(View.VISIBLE);
        TitleTerrariumEdit.setVisibility(View.GONE);
        TitleTerrarium.setText(TitleTerrariumEdit.getText().toString());
        temperatureMax.setVisibility(View.GONE);
        maxPicker.setVisibility(View.VISIBLE);
        temperatureMin.setVisibility(View.VISIBLE);
        minPicker.setVisibility(View.GONE);
        temperatureMin.setText(minPicker.getValue() + ".0");
        humiditePicker.setVisibility(View.GONE);
        humidite.setVisibility(View.VISIBLE);
        humidite.setText(humiditePicker.getValue()+".0");
        verification();

    }

    public void modifhumidite(View v){
        TitleTerrarium.setVisibility(View.VISIBLE);
        TitleTerrariumEdit.setVisibility(View.GONE);
        TitleTerrarium.setText(TitleTerrariumEdit.getText().toString());
        temperatureMax.setVisibility(View.VISIBLE);
        maxPicker.setVisibility(View.GONE);
        temperatureMax.setText(maxPicker.getValue() + ".0");
        temperatureMin.setVisibility(View.VISIBLE);
        minPicker.setVisibility(View.GONE);
        temperatureMin.setText(minPicker.getValue() + ".0");
        humiditePicker.setVisibility(View.VISIBLE);
        humidite.setVisibility(View.GONE);
        verification();
    }

    public void ChangerTitre(View v){
        TitleTerrarium.setVisibility(View.GONE);
        TitleTerrariumEdit.setVisibility(View.VISIBLE);
        temperatureMax.setVisibility(View.VISIBLE);
        maxPicker.setVisibility(View.GONE);
        temperatureMax.setText(maxPicker.getValue() + ".0");
        temperatureMin.setVisibility(View.VISIBLE);
        minPicker.setVisibility(View.GONE);
        temperatureMin.setText(minPicker.getValue() + ".0");
        humiditePicker.setVisibility(View.GONE);
        humidite.setVisibility(View.VISIBLE);
        humidite.setText(humiditePicker.getValue()+".0");
        verification();
    }

    public void clickTimeMin(View v){
        API.launchShortToast(getApplicationContext(),"onClick");
        TitleTerrarium.setVisibility(View.VISIBLE);
        TitleTerrariumEdit.setVisibility(View.GONE);
        TitleTerrarium.setText(TitleTerrariumEdit.getText().toString());
        temperatureMax.setVisibility(View.VISIBLE);
        maxPicker.setVisibility(View.GONE);
        temperatureMax.setText(maxPicker.getValue() + ".0");
        temperatureMin.setVisibility(View.VISIBLE);
        minPicker.setVisibility(View.GONE);
        temperatureMin.setText(minPicker.getValue() + ".0");
        humiditePicker.setVisibility(View.GONE);
        humidite.setVisibility(View.VISIBLE);
        humidite.setText(humiditePicker.getValue()+".0");
        timeMinPickerDialog=new TimePickerDialog(TerrariumActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                if(minutes<=9){
                    heureMinTerrarium.setText(hour+":0"+minutes+":00");
                }else{
                    heureMinTerrarium.setText(hour+":"+minutes+":00");
                }
                if(heureMinTerrarium.getText().toString().length() == 7)
                    heureMinTerrarium.setText("0" + heureMinTerrarium.getText().toString());
                verification();
            }
        },Integer.parseInt(heureMinTerrarium.getText().toString().split(":")[0]),Integer.parseInt(heureMinTerrarium.getText().toString().split(":")[1]),true);
        timeMinPickerDialog.show();

    }

    public void clickTimeMax(View v){
        API.launchShortToast(getApplicationContext(),"onClick");
        TitleTerrarium.setVisibility(View.VISIBLE);
        TitleTerrariumEdit.setVisibility(View.GONE);
        TitleTerrarium.setText(TitleTerrariumEdit.getText().toString());
        temperatureMax.setVisibility(View.VISIBLE);
        maxPicker.setVisibility(View.GONE);
        temperatureMax.setText(maxPicker.getValue() + ".0");
        temperatureMin.setVisibility(View.VISIBLE);
        minPicker.setVisibility(View.GONE);
        temperatureMin.setText(minPicker.getValue() + ".0");
        humiditePicker.setVisibility(View.GONE);
        humidite.setVisibility(View.VISIBLE);
        humidite.setText(humiditePicker.getValue()+".0");
        timeMaxPickerDialog=new TimePickerDialog(TerrariumActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                if(minutes<=9){
                    heureMaxTerrarium.setText(hour+":0"+minutes+":00");
                }else{
                    heureMaxTerrarium.setText(hour+":"+minutes+":00");
                }
                if(heureMaxTerrarium.getText().toString().length() == 7)
                    heureMaxTerrarium.setText("0" + heureMaxTerrarium.getText().toString());
                verification();
            }
        },Integer.parseInt(heureMaxTerrarium.getText().toString().split(":")[0]),Integer.parseInt(heureMaxTerrarium.getText().toString().split(":")[1]),true);
        timeMaxPickerDialog.show();
        verification();
    }

    //Affichage du bouton de sauvegarde

    public void verification(){
        if(!TitleTerrarium.getText().toString().equals(bt.getNameTerrarium()) || !Double.valueOf(temperatureMax.getText().toString()).equals(bt.getTemperatureMax()) || !Double.valueOf(temperatureMin.getText().toString()).equals(bt.getTemperatureMin()) || !Double.valueOf(humidite.getText().toString()).equals(bt.getHumidityTerrarium()) || !heureMinTerrarium.getText().toString().equals(bt.getStartLightTime()) || !heureMaxTerrarium.getText().toString().equals(bt.getStopLightTime()))
            save_button.setVisibility(View.VISIBLE);
        else
            save_button.setVisibility(View.INVISIBLE);
    }

    //Sauvegarde dans la base
    public void SaveModif(View v){
        updateInfoVue(v);
        bt.setNameTerrarium(TitleTerrarium.getText().toString());
        bt.setTemperatureMax(Double.valueOf(temperatureMax.getText().toString()));
        bt.setTemperatureMin(Double.valueOf(temperatureMin.getText().toString()));
        bt.setHumidityTerrarium(Double.valueOf(humidite.getText().toString()));
        bt.setStartLightTime(heureMinTerrarium.getText().toString());
        bt.setStopLightTime(heureMaxTerrarium.getText().toString());
        Call<Integer> retour = API.getInstance().simpleService.modifTerrarium(bt);
        retour.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                System.out.println("OK");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println("KO");
            }
        });

    }

    //Suppression du terrarium
    public void deleteTerrarium(View v){
        updateInfoVue(v);
        Call<Integer> retour = API.getInstance().simpleService.deleteTerrarium(bt);
        retour.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                System.out.println("OK");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println("KO");
            }
        });
        finish();
    }


}