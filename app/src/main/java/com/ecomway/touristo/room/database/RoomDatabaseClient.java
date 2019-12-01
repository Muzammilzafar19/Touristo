package com.ecomway.touristo.room.database;

import android.content.Context;

import androidx.room.Room;

public class RoomDatabaseClient {

    private Context mCtx;
    private static RoomDatabaseClient mInstance;

    //our app database object
    private RoomTouristoDB roomTouristoDB;
    private RoomDatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        //creating the app database with Room database builder
        //MyToDos is the name of the database
        roomTouristoDB = Room.databaseBuilder(mCtx, RoomTouristoDB.class, "RoomTouristoDB").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }
    public static synchronized RoomDatabaseClient getInstance(Context mCtx)
    {
        if (mInstance == null) {
            mInstance = new RoomDatabaseClient(mCtx);
        }
        return mInstance;
    }

    public RoomTouristoDB getAppDatabase() {
        return roomTouristoDB;
    }
}
