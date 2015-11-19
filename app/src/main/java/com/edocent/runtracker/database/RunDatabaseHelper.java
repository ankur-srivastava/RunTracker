package com.edocent.runtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.edocent.runtracker.model.Location;
import com.edocent.runtracker.model.Run;

/**
 * Created by Ankur on 11/19/2015.
 */
public class RunDatabaseHelper extends SQLiteOpenHelper{

    static final String DB_NAME="runtracker";
    static final int VERSION = 1;

    //Run
    static final String TABLE_RUN = "run";
    static final String COLUMN_RUN_START_DATE = "start_date";

    //Location
    static final String TABLE_LOCATION = "location";
    static final String COLUMN_LOCATION_LATITUDE = "latitude";
    static final String COLUMN_LOCATION_LONGITUDE = "longitude";
    static final String COLUMN_LOCATION_ALTITUDE = "altitude";
    static final String COLUMN_LOCATION_TIMESTAMP = "timestamp";
    static final String COLUMN_LOCATION_PROVIDER = "provider";
    static final String COLUMN_LOCATION_RUN_ID = "run_id";

    public RunDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "run" table
        db.execSQL("create table run (" +
                "_id integer primary key autoincrement, start_date integer)");
        // Create the "location" table
        db.execSQL("create table location (" +
                " timestamp integer, latitude real, longitude real, altitude real," +
                " provider varchar(100), run_id integer references run(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertRun(Run run){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RUN_START_DATE, run.getStartDate().getTime());
        return getWritableDatabase().insert(TABLE_RUN, null, cv);
    }

    public long insertLocation(long runId, Location location){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOCATION_LATITUDE, location.getLatitude());
        cv.put(COLUMN_LOCATION_LONGITUDE, location.getLongitude());
        cv.put(COLUMN_LOCATION_ALTITUDE, location.getAltitude());
        cv.put(COLUMN_LOCATION_TIMESTAMP, location.getTime());
        cv.put(COLUMN_LOCATION_PROVIDER, location.getProvider());
        cv.put(COLUMN_LOCATION_RUN_ID, runId);

        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);
    }
}