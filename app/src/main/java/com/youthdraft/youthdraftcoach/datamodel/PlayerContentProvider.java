package com.youthdraft.youthdraftcoach.datamodel;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.youthdraft.youthdraftcoach.datamodel.PlayerDB;
import com.youthdraft.youthdraftcoach.datamodel.PlayerDBHelper;

/**
 * Created by marty331 on 1/25/16.
 */
public class PlayerContentProvider extends ContentProvider {
    private static final String LOG_TAG = "PlayerContentProvider";

    private PlayerDBHelper mDbHelper;

    private static final int ALL_STORMS = 1;
    private static final int SINGLE_STORM = 2;

    private static final String AUTHORITY = "com.youthdraft.youthdraftcoach.playercontentprovider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/players");

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "players", ALL_STORMS);
        uriMatcher.addURI(AUTHORITY, "players/#", SINGLE_STORM);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PlayerDBHelper(getContext());
        return false;

    }

    // Return the MIME type corresponding to a content URI
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case ALL_STORMS:
                return "vnd.android.cursor.dir/vnd.com.youthdraft.youthdraftcoach.playercontentprovider.playerlist";
            case SINGLE_STORM:
                return "vnd.android.cursor.item/vnd.com.youthdraft.youthdraftcoach.playerontentprovider.playerlist";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    // The insert() method adds a new row to the appropriate table, using the
    // values
    // in the ContentValues argument. If a column name is not in the
    // ContentValues argument,
    // you may want to provide a default value for it either in your provider
    // code or in
    // your database schema.
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase dbi = mDbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_STORMS:
                // do nothing
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = dbi.insert(PlayerDB.DATABASE_TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        //Toast.makeText(AlertsContentProvider.this.getContext(), "New fence added ", Toast.LENGTH_SHORT).show();
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    // The query() method must return a Cursor object, or if it fails,
    // throw an Exception. If you are using an SQLite database as your data
    // storage,
    // you can simply return the Cursor returned by one of the query() methods
    // of the
    // SQLiteDatabase class. If the query does not match any rows, you should
    // return a
    // Cursor instance whose getCount() method returns 0. You should return null
    // only
    // if an internal error occurred during the query process.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase dbq = mDbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(PlayerDB.DATABASE_TABLE);

        switch (uriMatcher.match(uri)) {
            case ALL_STORMS:
                // do nothing
                break;
            case SINGLE_STORM:
                String id = uri.getPathSegments().get(1);
                Log.d("PCP", "ID = " + id);
                queryBuilder.appendWhere(PlayerDB.KEY_ROWID + "="+id ); //+ id
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(dbq, projection, selection,
                selectionArgs, null, null, sortOrder);

        return cursor;

    }



    // The delete() method deletes rows based on the selection or if an id is
    // provided then it deleted a single row. The methods returns the numbers
    // of records deleted from the database. If you choose not to delete the data
    // physically then just update a flag here.
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase dbd = mDbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_STORMS:
                // do nothing
                break;
            case SINGLE_STORM:
                String id = uri.getPathSegments().get(1);
                Log.d(LOG_TAG, "Delete started "+id);
                selection = PlayerDB.KEY_ROWID
                        + "="
                        + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
                        + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        //Log.d(LOG_TAG, "selection ="+selection+" selection ars="+selectionArgs);
        int deleteCount = dbd.delete(PlayerDB.DATABASE_TABLE, selection,
                selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        dbd.close();
        Log.d(LOG_TAG,"deleteCount ="+deleteCount);
        return deleteCount;
    }

    // The update method() is same as delete() which updates multiple rows
    // based on the selection or a single row if the row id is provided. The
    // update method returns the number of updated rows.
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "Update started: " + values);
        Log.d(LOG_TAG, "selection: "+selection);
        SQLiteDatabase dbu = mDbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_STORMS:
                // do nothing
                break;
            case SINGLE_STORM:
                String id = uri.getPathSegments().get(1);
                Log.d(LOG_TAG, "id ="+id);
                selection = PlayerDB.KEY_ROWID  + "=" + id;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = dbu.update(PlayerDB.DATABASE_TABLE, values,
                selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        dbu.close();
        Log.d(LOG_TAG, "selection: "+selection);
        Log.d(LOG_TAG, "updateCount ="+updateCount);

        return updateCount;
    }
}