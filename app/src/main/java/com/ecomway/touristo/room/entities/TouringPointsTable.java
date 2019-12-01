package com.ecomway.touristo.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TouringPointsTable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String touringPointName;
    private String tourCity;
    private String touringPointDescription;
    private String tourPointType;
    private String touringPointImagePath;
    private String tourLat;
    private String tourLng;

    public String getTourLat() {
        return tourLat;
    }

    public void setTourLat(String tourLat) {
        this.tourLat = tourLat;
    }

    public String getTourLng() {
        return tourLng;
    }

    public void setTourLng(String tourLng) {
        this.tourLng = tourLng;
    }

    public String getTourPointType() {
        return tourPointType;
    }

    public void setTourPointType(String tourPointType) {
        this.tourPointType = tourPointType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTouringPointName() {
        return touringPointName;
    }

    public void setTouringPointName(String touringPointName) {
        this.touringPointName = touringPointName;
    }

    public String getTourCity() {
        return tourCity;
    }

    public void setTourCity(String tourCity) {
        this.tourCity = tourCity;
    }

    public String getTouringPointDescription() {
        return touringPointDescription;
    }

    public void setTouringPointDescription(String touringPointDescription) {
        this.touringPointDescription = touringPointDescription;
    }

    public String getTouringPointImagePath() {
        return touringPointImagePath;
    }

    public void setTouringPointImagePath(String touringPointImagePath) {
        this.touringPointImagePath = touringPointImagePath;
    }
}
