package me.dcal.thermoconnectapp.Services;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;

public class User {
    @Json(name = "IdUser") public final int idUser;

    public User(int idUser) {
        this.idUser = idUser;
    }

    public int getIdUser() {
        return idUser;
    }


}
