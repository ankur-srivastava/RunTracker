package com.edocent.runtracker.utility;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Ankur on 11/17/2015.
 */
public class RunManager {

    public static final String ACTION_LOCATION = "com.edocent.android.runtracker.ACTION_LOCATION";
    private static final String TAG = "RunManager";
    Context mContext;
    LocationManager mLocationManager;
    static RunManager mRunManager;

    private RunManager(Context context){
        Log.v(TAG, "In RunManager");
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
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
}
