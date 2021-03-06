package me.dcal.thermoconnectapp.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.moshi.JsonAdapter;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import me.dcal.thermoconnectapp.R;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class API {
    private static final API instance=new API();
    public Retrofit retrofit;
    public SimpleService simpleService;
    public Moshi moshi;
    //TODO set url
    private static  final String urlFilePreference="me.dcal.thermoconnectapp.preferenceLogin";
    private static final String urlImageLogin="http://devmobile.dcal.me/pic";
    private static final String produrlBaseRetrofit="http://thermoconnect.dcal.me/";
    private static final String urlBaseRetrofit="http://devmobile.dcal.me/";
    private API(){
        //TODO mettre en place la création des services
        moshi=new Moshi.Builder().addLast(new KotlinJsonAdapterFactory()).build();
        retrofit = new Retrofit.Builder().baseUrl(produrlBaseRetrofit).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(MoshiConverterFactory.create(moshi)).build();
        simpleService=retrofit.create(SimpleService.class);
    }
    public static final void launchShortToast(Context context,String text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
    public static final void setStringToPreference(Context context,String key,String value){
        SharedPreferences sharedPref = context.getSharedPreferences(urlFilePreference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static final String getStringToPreference(Context context,String key,String value){
        SharedPreferences sharedPref = context.getSharedPreferences(urlFilePreference,Context.MODE_PRIVATE);
        return sharedPref.getString(key,"");
    }
    public static final BodyConnexion getBodyConnexion(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(urlFilePreference,Context.MODE_PRIVATE);
        String login= sharedPref.getString("login","");
        String password= sharedPref.getString("password","");
        if (login.length()>0 && password.length()>0){
            return new BodyConnexion(login,password);
        }
        return null;
    }
    public static final void setBodyConnexion(Context context,BodyConnexion body){
        SharedPreferences sharedPref = context.getSharedPreferences(urlFilePreference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(body==null){
            editor.putString("login", "");
            editor.putString("password", "");
            editor.apply();
        }else{
            editor.putString("login", body.login);
            editor.putString("password", body.password);
            editor.apply();
        }
    }
    public static final void getImageLogin(ImageView imageView){
        Uri uri=Uri.parse(produrlBaseRetrofit+"/pic"+"?id="+Math.random()*1000000);
        Glide.with(imageView).load(uri).apply(new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.errorconnexion)).into(imageView);
    }
    public static final API getInstance(){
        return instance;
    }
}
