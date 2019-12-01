package com.ecomway.touristo.model;

public class TouringPointsModel {
    private String touingPointName;
    private String description;
    private String imagePath;
    private String weather;

    public String getTouingPointName() {
        return touingPointName;
    }

    public void setTouingPointName(String touingPointName) {
        this.touingPointName = touingPointName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
