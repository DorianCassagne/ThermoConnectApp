package me.dcal.thermoconnectapp.Modeles;

import com.github.mikephil.charting.data.Entry;

import me.dcal.thermoconnectapp.Services.BodyConnexion;

public class BodyAnimalData implements Comparable<BodyAnimalData>{

    public BodyConnexion bodyConnexion;
    public Integer idAnimal;
    public String dateAnimalData;
    public Double weight;

    public BodyAnimalData(BodyConnexion bodyConnexion, Integer idAnimal, String dateAnimalData, Double weight) {
        this.bodyConnexion = bodyConnexion;
        this.idAnimal = idAnimal;
        this.dateAnimalData = dateAnimalData;
        this.weight = weight;
    }

    public BodyConnexion getBodyConnexion() {
        return bodyConnexion;
    }

    public void setBodyConnexion(BodyConnexion bodyConnexion) {
        this.bodyConnexion = bodyConnexion;
    }

    public Integer getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getDateAnimalData() {
        return dateAnimalData;
    }

    public void setDateAnimalData(String dateAnimalData) {
        this.dateAnimalData = dateAnimalData;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }



    @Override
    public int compareTo(BodyAnimalData o) {
        return getDateAnimalData().compareTo(o.getDateAnimalData());
    }
}
