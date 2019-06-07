package com.example.roy.cryptofolio;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {PortfolioDbObject.class}, version = 1)
public abstract class CryptoDatabase extends RoomDatabase {

    public abstract CryptoDao cryptoDao();

    private final static String NAME_DATABASE = "cryptocurrency_db";

    private static CryptoDatabase sInstance;

    public static CryptoDatabase getsInstance(Context context) {

        if(sInstance == null) {
            sInstance = Room.databaseBuilder(context, CryptoDatabase.class, NAME_DATABASE).allowMainThreadQueries().build();
        }
        return sInstance;
    }

}
