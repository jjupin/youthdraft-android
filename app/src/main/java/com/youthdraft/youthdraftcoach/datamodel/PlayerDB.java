package com.youthdraft.youthdraftcoach.datamodel;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by marty331 on 1/25/16.
 */
public class PlayerDB {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_PLAYERID = "playerid";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_MIDDLE = "middle_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_TRYOUT_DATE = "tryout_date";
    public static final String KEY_TRYOUT_TIME = "tryout_time";
    public static final String KEY_JERSEY = "jersey";
    public static final String KEY_PRIOR_LEVEL = "prior_play_level";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_LEAGUE_AGE = "league_age";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_LEAGUE_ID = "league_id";
    public static final String KEY_SPORT = "sport";
    public static final String KEY_PRIOR_TEAM = "prior_team";

    private static final int VER_LAUNCH = 1;

    public static final String LOG_TAG = "PlayerDB";
    public static final String DATABASE_TABLE = "players";

    private static final String DATABASE_CREATE = ("CREATE TABLE "
            + DATABASE_TABLE + " ( " + KEY_ROWID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_PLAYERID + " TEXT NOT NULL UNIQUE, " +
            KEY_FIRSTNAME + " TEXT NOT NULL, " +
            KEY_MIDDLE + " TEXT, "+
            KEY_LASTNAME + " TEXT NOT NULL, " +
            KEY_TRYOUT_DATE + " TEXT NOT NULL, " +
            KEY_TRYOUT_TIME + " TEXT NOT NULL, " +
            KEY_JERSEY + " TEXT NOT NULL, " +
            KEY_PRIOR_LEVEL + " TEXT, " +
            KEY_PRIOR_TEAM + " TEXT, " +
            KEY_BIRTHDAY + " TEXT NOT NULL, " +
            KEY_LEAGUE_AGE + " TEXT NOT NULL , " +
            KEY_GENDER + " TEXT NOT NULL, " +
            KEY_LEAGUE_ID + " TEXT NOT NULL, " +
            KEY_SPORT + " TEXT NOT NULL);");

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