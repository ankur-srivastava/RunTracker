package com.edocent.runtracker.utility;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.edocent.runtracker.model.RunLocation;

import java.util.Date;

/**
 * Created by Ankur on 11/19/2015.
 */
public class TrackingLocationReceiver extends LocationReceiver{

    private static final String TAG = TrackingLocationReceiver.class.getSimpleName();

    @Override
    protected void onLocationReceived(Context context, Location loc) {
        Log.v(TAG, "Insert Location Update");

        RunLocation runLocation = new RunLocation(loc.getLatitude(), loc.getLongitude());
        runLocation.setTime(new Date().getTime());
        runLocation.setAltitude(loc.getAltitude());

        Log.v(TAG, "Ready to insert following RunLocation object "+runLocation);

        RunManager.get(context).insertLocation(runLocation);
    }
}
