package me.dcal.thermoconnectapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.dcal.thermoconnectapp.Services.API;
import me.dcal.thermoconnectapp.Services.BodyConnexion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    ImageView image;
    TextView errorDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= findViewById(R.id.email);
        password=findViewById(R.id.password);
        errorDisplay=findViewById(R.id.erreurLogin);
        image=findViewById(R.id.imageView);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Page de connexion Thermoconnect");
        bindImage();
        setOnChangeEvent(email);
        setOnChangeEvent(password);
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
    public void afficheErreur(String erreur){
        errorDisplay.setText(erreur);
        errorDisplay.setVisibility(View.VISIBLE);
    }
    public void cacheErreur(View v){
        errorDisplay.setVisibility(View.GONE);
    }
    public void bindImage(){
        API.getImageLogin(image);
    }
    public void setOnChangeEvent(EditText text){
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                cacheErreur(text);
            }
        });
    }
    public void login(View view){
        cacheErreur(view);
        if(email.getText().length()>0 && password.getText().length()>0) {

            BodyConnexion body=new BodyConnexion(email.getText().toString(),password.getText().toString());
            Call<Boolean> id =API.getInstance().simpleService.connexion(body);
            id.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Boolean i=response.body();
                    if(i==null){
                        afficheErreur("Une erreur est survenu, veuillez reessayer plus tard");
                        API.launchShortToast(getApplicationContext(),"Une erreur est survenu, veuillez reessayer plus tard");
                    }
                    else if(true==i.booleanValue()){
                        API.setBodyConnexion(getApplicationContext(),body);
                        Intent intent = new Intent(getApplicationContext(), TerraListActivity.class);
                        startActivity(intent);
                    }
                    else{
                        afficheErreur("Le formulaire contient une erreur, la combinaison login/mot de passe est surement fausse");
                        API.launchShortToast(getApplicationContext(),"Le formulaire contient une erreur, la combinaison login/mot de passe est surement fausse");
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    call.request().url();
                    API.launchShortToast(getApplicationContext(),"La connexion avec le serveur rencontre un probleme");
                    afficheErreur("La connexion avec le serveur rencontre un probleme");
                }
            });
        }else{
            afficheErreur("Veuillez rentrer un login et mot de passe valide");
        }

    }
    public void inscription(View view){
        cacheErreur(view);
        if(email.getText().length()>0 && password.getText().length()>0) {
            BodyConnexion body = new BodyConnexion(email.getText().toString(), password.getText().toString());

            Call<Boolean> id = API.getInstance().simpleService.createuser(body);
            id.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Boolean i = response.body();
                    if(i==null){
                        afficheErreur("Une erreur est survenu, veuillez reessayer plus tard");
                        API.launchShortToast(getApplicationContext(),"Une erreur est survenu, veuillez reessayer plus tard");
                    }
                    else if (true == i.booleanValue()) {
                        API.setBodyConnexion(getApplicationContext(), body);
                        Intent intent = new Intent(getApplicationContext(), TerraListActivity.class);
                        startActivity(intent);
                    }
                    else{
                        afficheErreur("Le formulaire contient des erreurs, ou le login existe déjà");
                        API.launchShortToast(getApplicationContext(),"Le formulaire contient des erreurs, ou le login existe déjà");
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    call.request().url();
                    API.launchShortToast(getApplicationContext(),"La connexion avec le serveur rencontre un probleme");
                    afficheErreur("La connexion avec le serveur rencontre un probleme");
                }
            });
        }else{
            afficheErreur("Veuillez rentrer un login et mot de passe valide");
        }

    }
}