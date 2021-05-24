package me.dcal.thermoconnectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import me.dcal.thermoconnectapp.Services.API;
import me.dcal.thermoconnectapp.Services.BodyConnexion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= findViewById(R.id.email);
        password=findViewById(R.id.password);
    }
    @Override
    protected void onResume() {
        super.onResume();
        API.launchShortToast(getApplicationContext(),"resume");
        if(API.getBodyConnexion(getApplicationContext())!=null){
            Intent intent = new Intent(getApplicationContext(), TerraListActivity.class);
            startActivity(intent);
        }
    }
    public void login(View view){
        BodyConnexion body=new BodyConnexion(email.getText().toString(),password.getText().toString());
        Call<Boolean> id =API.getInstance().simpleService.connexion(body);
        id.enqueue(new Callback<Boolean>() {
                       @Override
                       public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                           Boolean i=response.body();
                           Toast toast = Toast.makeText(getApplicationContext(), i+"", Toast.LENGTH_SHORT);
                           toast.show();
                           if(true==i.booleanValue()){
                               API.setBodyConnexion(getApplicationContext(),body);
                               Intent intent = new Intent(getApplicationContext(), TerraListActivity.class);
                               startActivity(intent);
                           }
                       }

                       @Override
                       public void onFailure(Call<Boolean> call, Throwable t) {
                           call.request().url();
                           Toast toast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG);
                           toast.show();
                       }
                   });


    }
    public void inscription(View view){
        BodyConnexion body=new BodyConnexion(email.getText().toString(),password.getText().toString());
        Call<Boolean> id =API.getInstance().simpleService.createuser(body);
        id.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean i=response.body();
                Toast toast = Toast.makeText(getApplicationContext(), i+"", Toast.LENGTH_SHORT);
                toast.show();
                if(true==i.booleanValue()){
                    API.setBodyConnexion(getApplicationContext(),body);
                    Intent intent = new Intent(getApplicationContext(), TerraListActivity.class);
                    startActivity(intent);
                }
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