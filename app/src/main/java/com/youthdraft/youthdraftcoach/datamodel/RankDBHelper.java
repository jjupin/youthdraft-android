package com.youthdraft.youthdraftcoach.datamodel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.youthdraft.youthdraftcoach.datamodel.RankDB;

/**
 * Created by marty331 on 1/25/16.
 */
public class RankDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "rank";
    private static final int DATABASE_VERSION = 1;

    public RankDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        RankDB.onCreate(db);
        //db.execSQL(DATABASE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        RankDB.onUpgrade(db, oldVersion, newVersion);
    }
}