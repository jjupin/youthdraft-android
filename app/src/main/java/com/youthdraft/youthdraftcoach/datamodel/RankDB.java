package com.youthdraft.youthdraftcoach.datamodel;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by marty331 on 1/25/16.
 */
public class RankDB {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_PLAYID = "player_id";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_INITIAL = "initial";
    public static final String KEY_BIRTH = "birth";
    public static final String KEY_JERSEY = "jersey";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_SPEED = "speed";
    public static final String KEY_HITTING = "hitting";
    public static final String KEY_BAT_SPEED = "bat_speed";
    public static final String KEY_INFIELD = "infield";
    public static final String KEY_OUTFIELD = "outfield";
    public static final String KEY_THROWING = "throwing";
    public static final String KEY_ARM_STRENGTH = "arm_strength";
    public static final String KEY_BASE_RUNNING = "base_running";
    public static final String KEY_DRAFTABLE = "draftable";
    public static final String KEY_RATING = "rating";
    public static final String KEY_DATE = "date";
    public static final String KEY_NOTES = "notes";

    private static final int VER_LAUNCH = 1;

    public static final String LOG_TAG = "RankDB";
    public static final String DATABASE_TABLE = "rank";

    private static final String DATABASE_CREATE = ("CREATE TABLE "
            + DATABASE_TABLE + " ( " + KEY_ROWID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_PLAYID + " TEXT NOT NULL, " +
            KEY_FIRSTNAME + " TEXT NOT NULL, " +
            KEY_LASTNAME + " TEXT NOT NULL, " +
            KEY_INITIAL + " TEXT, " +
            KEY_BIRTH  + " TEXT, " +
            KEY_JERSEY + " TEXT, " +
            KEY_BAT_SPEED + " TEXT, " +
            KEY_HEIGHT + " TEXT, " +
            KEY_WEIGHT + " TEXT , " +
            KEY_SPEED + " TEXT , " +
            KEY_HITTING + " TEXT  , " +
            KEY_INFIELD + " TEXT  , " +
            KEY_OUTFIELD + " TEXT , " +
            KEY_BASE_RUNNING + " TEXT L, " +
            KEY_THROWING + " TEXT, " +
            KEY_ARM_STRENGTH + " TEXT , " +
            KEY_DRAFTABLE + " INTEGER , " +
            KEY_RATING + " REAL , " +
            KEY_DATE + " TEXT NOT NULL, " +
            KEY_NOTES + " TEXT);");

    public static void onCreate(SQLiteDatabase db) {
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }
    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will retain all old data");

        int version = oldVersion;
        /*
        switch (version) {
            case VER_LAUNCH:
                version = VER_TWO;

            case VER_TWO:
                // Version 5 added column for time.

                db.execSQL("ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN "
                        + KEY_ALERTED + " TEXT DEFAULT \'N\'");

                version = VER_THREE;
            case VER_THREE:
                version = VER_FOUR;
            case VER_FOUR:
                version = VER_FIVE;
            case VER_FIVE:
                version = VER_SIX;
            case VER_SIX:
                version = VER_SEVEN;
            case VER_SEVEN:
                version = VER_EIGHT;

            case VER_EIGHT:
                // Version 5 added column for time.
                db.execSQL("ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN "
                        + KEY_ENDED + " TEXT DEFAULT \'N\'");

        }
        */

    }

}