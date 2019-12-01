package com.ecomway.touristo.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ecomway.touristo.room.entities.TouringPointsTable;

import java.util.List;

@Dao
public interface TouringPointsTableDao {
    @Insert
    long insertAll(TouringPointsTable touringPointsTable);

    @Query("select * from touringpointstable")
    List<TouringPointsTable> getAll();

    @Query("delete from touringpointstable")
    void deleteAll();

    @Query("Select * from touringpointstable where touringPointName=:touringPointName")
    TouringPointsTable getSpecificTour(String touringPointName);

    @Query("Select * from touringpointstable where tourPointType=:tourPointType")
    List<TouringPointsTable>  getSpecificTourByType(String tourPointType);

    @Query("Select * from touringpointstable where tourCity=:tourCity")
    List<TouringPointsTable>  getSpecificTourByCityServerId(String tourCity);
}
