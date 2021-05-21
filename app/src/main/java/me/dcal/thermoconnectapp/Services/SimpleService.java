package me.dcal.thermoconnectapp.Services;

import kotlinx.coroutines.Deferred;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SimpleService {
   @POST("connexion")
   public Call<Boolean> connexion(@Body BodyConnexion bodyConnexion);
   /*@Multipart
   @POST("")
   public Call<Object> test(@Part BodyConnexion bodyConnexion);*/
}
