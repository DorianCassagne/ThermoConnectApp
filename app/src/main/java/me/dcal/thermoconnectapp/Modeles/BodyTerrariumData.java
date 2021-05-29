package me.dcal.thermoconnectapp.Modeles;

import me.dcal.thermoconnectapp.Services.BodyConnexion;

public class BodyTerrariumData {

    public BodyConnexion bodyConnexion;
    public Integer idTerrarium;
    public String date;
    public Double temperature;
    public Double humidity;

    public BodyTerrariumData(BodyConnexion bodyConnexion,Integer idTerra,  String date, Double temperature, Double humidity) {
        this.bodyConnexion = bodyConnexion;
        this.idTerrarium = idTerra;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public BodyConnexion getBodyConnexion() {
        return bodyConnexion;
    }

    public void setBodyConnexion(BodyConnexion bodyConnexion) {
        this.bodyConnexion = bodyConnexion;
    }

    public Integer getId() {
        return idTerrarium;
    }

    public void setId(Integer id) {
        this.idTerrarium = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }
}
