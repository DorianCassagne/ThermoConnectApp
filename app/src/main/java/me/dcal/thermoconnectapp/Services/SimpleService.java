package me.dcal.thermoconnectapp.Services;

import me.dcal.thermoconnectapp.Modeles.BodyTerrarium;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SimpleService {
   @POST("connexion")
   public Call<Boolean> connexion(@Body BodyConnexion bodyConnexion);
   @POST("createuser")
   public Call<Boolean> createuser(@Body BodyConnexion bodyConnexion);
   @POST("ajoutTerrarium")
   public Call<Integer> ajoutTerrarium(@Body BodyTerrarium bodyTerrarium);
   /*@Multipart
   @POST("")
   public Call<Object> test(@Part BodyConnexion bodyConnexion);*/
}
