package me.dcal.thermoconnectapp.Modeles;

import java.sql.Time;

import me.dcal.thermoconnectapp.Services.BodyConnexion;

public class BodyTerrarium {
    public BodyConnexion bodyConnexion;
    public String nameTerrarium;
    public String size;
    public String startLightTime;
    public String stopLightTime;
    public int temperatureMax;
    public int temperatureMin;
    public int humidity;

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(int temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public int getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(int temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
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

    public BodyTerrarium(BodyConnexion bodyConnexion, String nameTerrarium, String size, String startLightTime, String stopLightTime, int temperatureMax, int temperatureMin, int humidity) {
        this.bodyConnexion = bodyConnexion;
        this.nameTerrarium = nameTerrarium;
        this.size = size;
        this.startLightTime = startLightTime;
        this.stopLightTime = stopLightTime;
        this.temperatureMax = temperatureMax;
        this.temperatureMin = temperatureMin;
        this.humidity = humidity;
    }
}
