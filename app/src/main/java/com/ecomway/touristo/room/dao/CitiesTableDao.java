package com.ecomway.touristo.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ecomway.touristo.room.entities.CitiesTable;

import java.util.List;

@Dao
public interface CitiesTableDao {
    @Insert
    void insertAll(CitiesTable citiesTable);

    @Query("select * from citiestable")
    List<CitiesTable> getAllCities();

    @Query("delete from citiestable")
    void deleteAll();

   @Query("Select * from citiestable where cityName=:cityname")
    CitiesTable getSpecificCity(String cityname);

    @Query("Select * from citiestable where cityIdFromServer=:cityIdFromServer")
    CitiesTable getSpecificCityByServerId(String cityIdFromServer);
}
