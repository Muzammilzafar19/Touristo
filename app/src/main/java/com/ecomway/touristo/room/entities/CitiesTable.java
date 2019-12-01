package com.ecomway.touristo.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CitiesTable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String cityName;
    private String cityIdFromServer;
    private String cityDescription;
    private String cityImagePath;
    private String cityLat;
    private String cityLng;

    public String getCityLat() {
        return cityLat;
    }

    public void setCityLat(String cityLat) {
        this.cityLat = cityLat;
    }

    public String getCityLng() {
        return cityLng;
    }

    public void setCityLng(String cityLng) {
        this.cityLng = cityLng;
    }

    public String getCityIdFromServer() {
        return cityIdFromServer;
    }

    public void setCityIdFromServer(String cityIdFromServer) {
        this.cityIdFromServer = cityIdFromServer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityDescription() {
        return cityDescription;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
    }

    public String getCityImagePath() {
        return cityImagePath;
    }

    public void setCityImagePath(String cityImagePath) {
        this.cityImagePath = cityImagePath;
    }
}
