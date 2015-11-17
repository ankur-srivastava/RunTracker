package com.edocent.runtracker.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Ankur on 11/17/2015.
 */
public class LocationReceiver extends BroadcastReceiver{

    private static final String TAG = "LocationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = (Location) intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if(location != null){
            onLocationReceived(context, location);
        }
        if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)) {
            boolean enabled = intent
                    .getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnabledChanged(enabled);
        }
    }

    private void onLocationReceived(Context context, Location location) {
        Log.v(TAG, "Got the following latitude "+location.getLatitude()+" and longitude "+location.getLongitude());
    }
    protected void onProviderEnabledChanged(boolean enabled) {
        Log.d(TAG, "Provider " + (enabled ? "enabled" : "disabled"));
    }
}
