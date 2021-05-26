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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private TextView heureMaxTerrarium;
    private TextView heureMinTerrarium;
    private TextView TitleTerrarium;
    private EditText TitleTerrariumEdit;
    private LinearLayout globalLayout;
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
        heureMaxTerrarium = (TextView)findViewById(R.id.HeureMaxTerrarium);
        heureMinTerrarium = (TextView)findViewById(R.id.HeureMinTerrarium);
        TitleTerrarium = (TextView)findViewById(R.id.TitleTerrarium);
        TitleTerrariumEdit = (EditText)findViewById(R.id.TitleTerrariumEdit);
        save_button = (Button)findViewById(R.id.save_button);
        TitleTerrariumEdit.setText(bt.nameTerrarium);
        if(!TitleTerrariumEdit.hasFocus())
            TitleTerrarium.setText(TitleTerrariumEdit.getText().toString());


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

    public void clickTimeMin(View v){
        API.launchShortToast(getApplicationContext(),"onClick");
        timeMinPickerDialog=new TimePickerDialog(TerrariumActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                if(minutes<=9){
                    heureMinTerrarium.setText(hour+":0"+minutes);
                }else{
                    heureMinTerrarium.setText(hour+":"+minutes);
                }
            }
        },Integer.parseInt(heureMinTerrarium.getText().toString().split(":")[0]),Integer.parseInt(heureMinTerrarium.getText().toString().split(":")[1]),true);
        timeMinPickerDialog.show();
    }
    public void clickTimeMax(View v){
        API.launchShortToast(getApplicationContext(),"onClick");
        timeMaxPickerDialog=new TimePickerDialog(TerrariumActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                if(minutes<=9){
                    heureMaxTerrarium.setText(hour+":0"+minutes);
                }else{
                    heureMaxTerrarium.setText(hour+":"+minutes);
                }

            }
        },Integer.parseInt(heureMaxTerrarium.getText().toString().split(":")[0]),Integer.parseInt(heureMaxTerrarium.getText().toString().split(":")[1]),true);
        timeMaxPickerDialog.show();
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

    public void ChangerTitre(View v){
        TitleTerrarium.setWidth(0);
        TitleTerrarium.setHeight(0);
        TitleTerrariumEdit.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        TitleTerrariumEdit.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}