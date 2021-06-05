package me.dcal.thermoconnectapp;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.dcal.thermoconnectapp.Modeles.BodyAnimal;
import me.dcal.thermoconnectapp.Modeles.BodyTerrarium;
import me.dcal.thermoconnectapp.Modeles.TerraListData;
import me.dcal.thermoconnectapp.Services.API;
import me.dcal.thermoconnectapp.Services.BodyConnexion;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlimentationActivity extends AppCompatActivity {


    ListView alimentationlist;
    Button addalimentation;
    Button save;
    TextView editalim;
    TextView alimentation;
    AlimentationAdapter<String> arrayAdapter ;
    BodyAnimal bodyAnimal;

    String alimentationData="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arrayAdapter = new AlimentationAdapter<String>(AlimentationActivity.this, R.layout.alimentation_row);


        alimentationlist = (ListView) findViewById(R.id.alimentationlist);
        addalimentation = (Button) findViewById(R.id.addAlimentation);
        save = (Button) findViewById(R.id.alimSave);


        addalimentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
                arrayAdapter.add(formatter.format(new Date()));
                alimentationlist.setAdapter(arrayAdapter);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MultipartBody.Part> part = new ArrayList<>();

                if (alimentationData.endsWith(";"))
                    alimentationData = alimentationData.substring(0, alimentationData.length()-1);
                bodyAnimal.setFood(alimentationData);
                Call<Integer> retour = API.getInstance().simpleService.modifAnimal(bodyAnimal,part);
                retour.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.body()>=0){
                            save.setVisibility(View.GONE);
                        }else{
                            Toast toasts = Toast.makeText(getApplicationContext(), "Erreur durant la mise à jour veuillez essayer plus tard", Toast.LENGTH_SHORT);
                            toasts.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        System.out.println("KO");
                    }
                });
            }
        });


        generationPage();

    }

    public void setAlimentationRow(String alimentationRow) {
        // do what you want with invoiceId

        alimentationData += alimentationRow+";";


        save.setVisibility(View.VISIBLE);
    }

    public void removeAlimentationRow(String alimentationRow){
        if (alimentationRow.equals(alimentationData)){
            alimentationData = "";
        }else{
            String[] data = alimentationData.split(";"+alimentationRow.replace("|", "\\|"));
            alimentationData = "";
            for (int i =0; i<data.length;i++){
                alimentationData +=data[i];
            }
            if (alimentationData.endsWith(";")){
                alimentationData = alimentationData.substring(0, alimentationData.length() - 1);
            }
        }

        save.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {

        Intent i=new Intent(getApplicationContext(), AnimalActivity.class);
        i.putExtra("alimentation", alimentationData);
        setResult(-1, i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        generationPage();
    }

    public void generationPage(){
        arrayAdapter = new AlimentationAdapter<String>(this, R.layout.alimentation_row);

        bodyAnimal = (BodyAnimal) getIntent().getSerializableExtra("bodyanimal");
        alimentationData = bodyAnimal.getFood() == null ? "" : bodyAnimal.getFood();
        String alim = bodyAnimal.getFood();
        if (alim != null){
            String[] datas = alim.split(";");
            for (String data : datas){
                if (data != "")
                    arrayAdapter.add(data);
            }
            alimentationlist.setAdapter(arrayAdapter);
        }

    }
}
