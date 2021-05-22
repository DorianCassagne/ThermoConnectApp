package me.dcal.thermoconnectapp;

import android.net.Uri;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FileActivity extends AppCompatActivity {
    public Uri getUrl() {
        return url;
    }

    public void setUrl(Uri url) {
        this.url = url;
    }

    public FileActivity(Uri url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri url;
    public String name;


}
