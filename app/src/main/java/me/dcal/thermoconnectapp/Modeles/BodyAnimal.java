package me.dcal.thermoconnectapp.Modeles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.dcal.thermoconnectapp.Services.BodyConnexion;

public class BodyAnimal implements Serializable {

    public BodyConnexion bodyConnexion;
    public int idAnimal;
    public int terrarium;
    public BodySpecies species;
    public String name;
    public Boolean sex;
    public String dateOfBirth;
    public String description;
    public String food;
    public int weight;
    public  List<String> documents =  new ArrayList<>();

    public BodyAnimal(BodyConnexion bodyConnexion, int terrarium, BodySpecies species, String name, Boolean sex, String dateOfBirth, String description, String food, int weight, List<String> documents) {
        this.bodyConnexion = bodyConnexion;
        this.idAnimal = idAnimal;
        this.terrarium = terrarium;
        this.species = species;
        this.name = name;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.description = description;
        this.food = food;
        this.weight = weight;
        this.documents = documents;
    }

    public BodyConnexion getBodyConnexion() {
        return bodyConnexion;
    }

    public void setBodyConnexion(BodyConnexion bodyConnexion) {
        this.bodyConnexion = bodyConnexion;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public int getTerrarium() {
        return terrarium;
    }

    public void setTerrarium(int terrarium) {
        this.terrarium = terrarium;
    }

    public BodySpecies getSpecies() {
        return species;
    }

    public void setSpecies(BodySpecies species) {
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
