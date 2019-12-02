package com.ecomway.touristo.room.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ecomway.touristo.room.dao.CitiesTableDao;
import com.ecomway.touristo.room.dao.TouringPointsTableDao;
import com.ecomway.touristo.room.entities.CitiesTable;
import com.ecomway.touristo.room.entities.TouringPointsTable;

@Database(entities = {CitiesTable.class, TouringPointsTable.class},exportSchema = false, version = 2)
public abstract class RoomTouristoDB extends RoomDatabase {
    public abstract CitiesTableDao citiesTableDao();
    public abstract TouringPointsTableDao touringPointsTableDao();
}
