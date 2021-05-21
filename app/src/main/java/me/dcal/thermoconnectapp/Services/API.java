package me.dcal.thermoconnectapp.Services;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class API {
    private static final API instance=new API();
    public Retrofit retrofit;
    public SimpleService simpleService;
    public Moshi moshi;
    //TODO set url
    private static final String urlBaseRetrofit="http://devmobile.dcal.me/";
    private API(){
        //TODO mettre en place la création des services
        moshi=new Moshi.Builder().addLast(new KotlinJsonAdapterFactory()).build();
        moshi.adapter(User.class);
        retrofit = new Retrofit.Builder().baseUrl(urlBaseRetrofit).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(MoshiConverterFactory.create(moshi)).build();
        simpleService=retrofit.create(SimpleService.class);
    }
    public static final API getInstance(){
        return instance;
    }
}
