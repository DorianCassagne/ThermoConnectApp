package me.dcal.thermoconnectapp.Modeles;

import java.io.Serializable;

public class BodySpecies implements Serializable {

    public String species;
    public String description;
    public Double humidity;
    public Double tempMin;
    public Double tempMax;

    public BodySpecies(String species, String description, Double humidity, Double tempMin, Double tempMax) {
        this.species = species;
        this.description = description;
        this.humidity = humidity;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    @Override
    public String toString() {
        return  this.species;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
