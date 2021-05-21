package me.dcal.thermoconnectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import kotlinx.coroutines.Deferred;
import me.dcal.thermoconnectapp.Services.API;
import me.dcal.thermoconnectapp.Services.BodyConnexion;
import me.dcal.thermoconnectapp.Services.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        Call<Boolean> id =API.getInstance().simpleService.connexion(new BodyConnexion("test","test"));
        id.enqueue(new Callback<Boolean>() {
                       @Override
                       public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                           Boolean i=response.body();
                               Toast toast = Toast.makeText(getApplicationContext(), i+"", Toast.LENGTH_LONG);
                               toast.show();
                           Intent intent = new Intent(getApplicationContext(), TerraListActivity.class);
                           startActivity(intent);

                       }

                       @Override
                       public void onFailure(Call<Boolean> call, Throwable t) {
                           call.request().url();
                           Toast toast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG);
                           toast.show();
                       }
                   });


    }
}