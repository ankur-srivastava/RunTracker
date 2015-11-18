package com.edocent.runtracker;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.edocent.runtracker.model.Run;
import com.edocent.runtracker.utility.LocationReceiver;
import com.edocent.runtracker.utility.RunManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class RunFragment extends Fragment {

    private static final String TAG = "RunFragment";
    private RunManager runManager;
    private Button mStartButton, mStopButton;
    private TextView mStartedTextView, mLatitudeTextView,
            mLongitudeTextView, mAltitudeTextView, mDurationTextView;
    private Location mLastLocation;
    private Run mRun;

    private BroadcastReceiver broadcastReceiver = new LocationReceiver(){

        @Override
        protected void onLocationReceived(Context context, Location loc) {
            Log.v(TAG, "Got the following latitude "+loc.getLatitude()+" and longitude "+loc.getLongitude());
            mLastLocation = loc;
            if (isVisible())
                updateUI();
        }

        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
            int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
        }

    };

    public RunFragment() { }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setRetainInstance(true);
        runManager = RunManager.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);

        mStartedTextView = (TextView)view.findViewById(R.id.run_startedTextView);
        mLatitudeTextView = (TextView)view.findViewById(R.id.run_latitudeTextView);
        mLongitudeTextView = (TextView)view.findViewById(R.id.run_longitudeTextView);
        mAltitudeTextView = (TextView)view.findViewById(R.id.run_altitudeTextView);
        mDurationTextView = (TextView)view.findViewById(R.id.run_durationTextView);

        mStartButton = (Button)view.findViewById(R.id.run_startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runManager.startLocationUpdates();
                updateUI();
            }
        });

        mStopButton = (Button)view.findViewById(R.id.run_stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runManager.stopLocationUpdates();
                updateUI();
            }
        });

        updateUI();
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(RunManager.ACTION_LOCATION));
    }

    @Override
    public void onStop(){
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private void updateUI() {
        boolean started = runManager.isTrackingRun();

        if (mRun != null)
            mStartedTextView.setText(mRun.getStartDate().toString());

        int durationSeconds = 0;
        if (mRun != null && mLastLocation != null) {
            durationSeconds = mRun.getDurationSeconds(mLastLocation.getTime());
            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mLastLocation.getAltitude()));
        }
        mDurationTextView.setText(Run.formatDuration(durationSeconds));

        mStartButton.setEnabled(!started);
        mStopButton.setEnabled(started);
    }
}