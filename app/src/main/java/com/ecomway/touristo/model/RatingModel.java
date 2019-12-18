package com.ecomway.touristo.model;

public class RatingModel {
    private String userId;
    private String tourPointName;
    private String rating;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTourPointName() {
        return tourPointName;
    }

    public void setTourPointName(String tourPointName) {
        this.tourPointName = tourPointName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
