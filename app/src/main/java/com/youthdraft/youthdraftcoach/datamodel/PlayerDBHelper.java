package com.youthdraft.youthdraftcoach.datamodel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.youthdraft.youthdraftcoach.datamodel.PlayerDB;

/**
 * Created by marty331 on 1/25/16.
 */
public class PlayerDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "players";
    private static final int DATABASE_VERSION = 1;

    public PlayerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PlayerDB.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PlayerDB.onUpgrade(db, oldVersion, newVersion);
    }
}