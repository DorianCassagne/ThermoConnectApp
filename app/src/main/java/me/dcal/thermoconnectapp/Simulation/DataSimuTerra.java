package me.dcal.thermoconnectapp.Simulation;

import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import me.dcal.thermoconnectapp.Modeles.BodyTerrarium;
import me.dcal.thermoconnectapp.Modeles.BodyTerrariumData;
import me.dcal.thermoconnectapp.Services.API;
import me.dcal.thermoconnectapp.Services.BodyConnexion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSimuTerra {


    private BodyTerrarium bodyterra;

    public DataSimuTerra(BodyTerrarium bodyterra) {
        this.bodyterra = bodyterra;
    }


    public void simulation(){
        //BodyConnexion bodyConnexion, String date, Double temperature, Double humidity
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        double rangeMinTemp = this.getBodyterra().getTemperatureMin();
        double rangeMaxTemp = this.getBodyterra().getTemperatureMax();
        Random rTemp = new Random();
        double randomValueTemp = rangeMinTemp + (rangeMaxTemp - rangeMinTemp) * rTemp.nextDouble();

        double rangeMinHumidite = this.getBodyterra().getHumidityTerrarium();
        Random rHumidite = new Random();
        double randomValueHumidite = rangeMinHumidite + ((rangeMinHumidite+40) - rangeMinHumidite) * rHumidite.nextDouble();

        BodyTerrariumData data = new BodyTerrariumData(this.bodyterra.getBodyConnexion(),this.bodyterra.getIdTerrarium(),currentDateandTime, randomValueTemp,randomValueHumidite);
        Call<Integer> reponse= API.getInstance().simpleService.simuSetDataTerra(data);
        reponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
               Call<Integer> i = reponse;
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
               call.request().url();
          }
       });
    }
    public BodyTerrarium getBodyterra() {
        return bodyterra;
    }

    public void setBodyterra(BodyTerrarium bodyterra) {
        this.bodyterra = bodyterra;
    }
}
