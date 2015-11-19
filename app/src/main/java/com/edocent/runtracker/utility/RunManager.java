package com.edocent.runtracker.utility;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.edocent.runtracker.database.RunDatabaseHelper;
import com.edocent.runtracker.model.Run;
import com.edocent.runtracker.model.RunLocation;

/**
 * Created by Ankur on 11/17/2015.
 */
public class RunManager {

    public static final String ACTION_LOCATION = "com.edocent.android.runtracker.ACTION_LOCATION";
    static final String TAG = "RunManager";
    static final String PREFS_FILE = "runtracker";
    static final String PREF_CURRENT_RUN_ID = "currentRunId";
    Context mContext;
    LocationManager mLocationManager;
    static RunManager mRunManager;
    RunDatabaseHelper runDatabaseHelper;
    SharedPreferences sharedPreferences;
    long currentRunId;

    private RunManager(Context context){
        Log.v(TAG, "In RunManager");
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        runDatabaseHelper = new RunDatabaseHelper(context);
        sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    public static RunManager get(Context c){
        if(mRunManager == null){
            mRunManager = new RunManager(c.getApplicationContext());
        }
        return mRunManager;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate){
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mContext, 0, broadcast, flags);
    }

    public void startLocationUpdates() {
        //Network provider is suggested
        //String provider = LocationManager.GPS_PROVIDER;
        String provider = LocationManager.NETWORK_PROVIDER;

        Location startLocation = mLocationManager.getLastKnownLocation(provider);

        if(startLocation != null){
            Log.v(TAG, "Got the Start location");
            startLocation.setTime(System.currentTimeMillis());
            broadcastLocation(startLocation);
        }

        PendingIntent pi = getLocationPendingIntent(true);
        Log.v(TAG, "Going to request Location updates");
        mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
    }

    private void broadcastLocation(Location startLocation) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, startLocation);
        mContext.sendBroadcast(broadcast);
    }

    public void stopLocationUpdates() {
        Log.v(TAG, "Going to stop Location updates");
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null) {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    public boolean isTrackingRun() {
        return getLocationPendingIntent(false) != null;
    }

    public Run startNewRun(){
        Run run = insertRun();
        startTrackingRun(run);
        return run;
    }

    private void startTrackingRun(Run run) {
        currentRunId = run.get_id();
        sharedPreferences.edit().putLong(PREF_CURRENT_RUN_ID, currentRunId);
        startLocationUpdates();
    }

    public void stopRun(){
        stopLocationUpdates();
        currentRunId = 0;
        sharedPreferences.edit().remove(PREF_CURRENT_RUN_ID).commit();
    }

    private Run insertRun() {
        Run run = new Run();
        run.set_id(runDatabaseHelper.insertRun(run));
        return run;
    }

    public void insertLocation(RunLocation location){
        if (currentRunId != -1) {
            runDatabaseHelper.insertLocation(currentRunId, location);
        } else {
            Log.e(TAG, "Location received with no tracking run; ignoring.");
        }
    }
}
