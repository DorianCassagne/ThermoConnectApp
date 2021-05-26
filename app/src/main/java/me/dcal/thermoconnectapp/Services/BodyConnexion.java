package me.dcal.thermoconnectapp.Services;

import java.io.Serializable;

public class BodyConnexion implements Serializable {
    public String login;
    public String password;

    public BodyConnexion(String login, String password) {
        this.login = login;
        this.password = password;

    }

    @Override
    public String toString() {
        return "BodyConnexion{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
