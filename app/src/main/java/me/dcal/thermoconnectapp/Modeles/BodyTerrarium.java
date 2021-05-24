package me.dcal.thermoconnectapp.Modeles;

import java.sql.Time;

import me.dcal.thermoconnectapp.Services.BodyConnexion;

public class BodyTerrarium {
    public BodyConnexion bodyConnexion;
    public String nameTerrarium;
    public String sizeTerrarium;
    public String startLightTime;
    public String stopLightTime;
    public Double temperatureMax;
    public Double temperatureMin;
    public Double humidityTerrarium;
    public Integer idTerrarium;

    public BodyTerrarium(BodyConnexion bodyConnexion, String nameTerrarium, String sizeTerrarium, String startLightTime, String stopLightTime, Double temperatureMax, Double temperatureMin, Double humidityTerrarium) {
        this.bodyConnexion = bodyConnexion;
        this.nameTerrarium = nameTerrarium;
        this.sizeTerrarium = sizeTerrarium;
        this.startLightTime = startLightTime;
        this.stopLightTime = stopLightTime;
        this.temperatureMax = temperatureMax;
        this.temperatureMin = temperatureMin;
        this.humidityTerrarium = humidityTerrarium;
    }

    public BodyConnexion getBodyConnexion() {
        return bodyConnexion;
    }

    public void setBodyConnexion(BodyConnexion bodyConnexion) {
        this.bodyConnexion = bodyConnexion;
    }

    public String getNameTerrarium() {
        return nameTerrarium;
    }

    public void setNameTerrarium(String nameTerrarium) {
        this.nameTerrarium = nameTerrarium;
    }

    public String getSizeTerrarium() {
        return sizeTerrarium;
    }

    public void setSizeTerrarium(String sizeTerrarium) {
        this.sizeTerrarium = sizeTerrarium;
    }

    public String getStartLightTime() {
        return startLightTime;
    }

    public void setStartLightTime(String startLightTime) {
        this.startLightTime = startLightTime;
    }

    public String getStopLightTime() {
        return stopLightTime;
    }

    public void setStopLightTime(String stopLightTime) {
        this.stopLightTime = stopLightTime;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Double getHumidityTerrarium() {
        return humidityTerrarium;
    }

    public void setHumidityTerrarium(Double humidityTerrarium) {
        this.humidityTerrarium = humidityTerrarium;
    }

    public Integer getIdTerrarium() {
        return idTerrarium;
    }

    public void setIdTerrarium(Integer idTerrarium) {
        this.idTerrarium = idTerrarium;
    }
}
