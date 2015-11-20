package com.edocent.runtracker;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.edocent.runtracker.adapter.RunAdapter;
import com.edocent.runtracker.database.RunDatabaseHelper;
import com.edocent.runtracker.model.RunLocation;

import java.util.ArrayList;
import java.util.List;

public class RunDetailFragment extends Fragment {

    private static final String TAG = RunDetailFragment.class.getSimpleName();
    OnFragmentInteractionListener mListener;
    int runId;
    ListView locationListView;

    private RunDatabaseHelper helper;
    private CursorAdapter cursorAdapter;
    Cursor tempLocationCursor;

    public RunDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run_detail, container, false);

        Log.v(TAG, "Get Location Cursor Adapter for the following runId "+runId);
        helper = new RunDatabaseHelper(getActivity());
        cursorAdapter = new RunAdapter(getActivity(), RunDatabaseHelper.getRunLocations(runId, helper, tempLocationCursor), 0);

        locationListView = (ListView) view.findViewById(R.id.runLocationsListViewId);

        if(cursorAdapter != null){
            Log.v(TAG, "Set Cursor Adapter");
            locationListView.setAdapter(cursorAdapter);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        helper = null;
        cursorAdapter = null;
        tempLocationCursor = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }
}