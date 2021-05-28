package me.dcal.thermoconnectapp.Modeles;

import me.dcal.thermoconnectapp.Services.BodyConnexion;

public class BodyTerrariumData {

    public BodyConnexion bodyConnexion;
    public BodyTerrarium bodyTerrarium;
    public Integer id;
    public String date;
    public Double temperature;
    public Double humidity;

    public BodyTerrariumData(BodyConnexion bodyConnexion, String date, Double temperature, Double humidity) {
        this.bodyConnexion = bodyConnexion;
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
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
