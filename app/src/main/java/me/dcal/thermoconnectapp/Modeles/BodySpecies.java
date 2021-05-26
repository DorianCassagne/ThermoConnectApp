package me.dcal.thermoconnectapp.Modeles;

import java.io.Serializable;

public class BodySpecies implements Serializable {

    public String species;
    public String description;

    public BodySpecies(String species, String description) {
        this.species = species;
        this.description = description;
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
