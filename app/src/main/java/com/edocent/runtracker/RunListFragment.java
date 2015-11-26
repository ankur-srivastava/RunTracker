package com.edocent.runtracker;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.edocent.runtracker.adapter.RunAdapter;
import com.edocent.runtracker.database.RunDatabaseHelper;
import com.edocent.runtracker.dummy.DummyContent;
import com.edocent.runtracker.loaders.SQLiteCursorLoader;

/**
 * @author Ankur
 */
public class RunListFragment extends ListFragment implements LoaderManager.LoaderCallbacks{

    static final int REQUEST_NEW_RUN = 0;
    static final String TAG = "RunListFragment";

    ListView runListView;
    OnFragmentInteractionListener mListener;
    private RunDatabaseHelper helper;
    private CursorAdapter cursorAdapter;
    Cursor tempCursor;

    public RunListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.run_list_fragment, container, false);

        Log.v(TAG, "Get Cursor Adapter");
        helper = new RunDatabaseHelper(getActivity());
        runListView = (ListView) view.findViewById(R.id.runListViewId);
        /* Cursor now loads via Loader : see onLoadFinished
        cursorAdapter = new RunAdapter(getActivity(), RunDatabaseHelper.getRuns(helper, tempCursor), 0);
        if(cursorAdapter != null){
            Log.v(TAG, "Set Cursor Adapter");
            runListView.setAdapter(cursorAdapter);
        }
        */

        //Initialize the Loader
        Log.v(TAG, "Initialize the Loader");
        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.run_main, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addRun) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivityForResult(intent, REQUEST_NEW_RUN);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (REQUEST_NEW_RUN == requestCode) {
            if(tempCursor != null){
                tempCursor.requery();
                ((CursorAdapter)getListAdapter()).notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
        helper = null;
        cursorAdapter = null;
        tempCursor = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        int _id = (int) id;
        Log.v(TAG, "Following run id was clicked "+_id);
        if (null != mListener) {
            mListener.onFragmentInteraction(_id);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.v(TAG, "Create Loader");
        return new RunListCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader loader, Object obj) {
        // Create an adapter to point at this cursor
        Log.v(TAG, "On Load Finished");
        cursorAdapter = new RunAdapter(getActivity(), (Cursor)obj, 0);

        if(cursorAdapter != null){
            Log.v(TAG, "Set Cursor Adapter");
            runListView.setAdapter(cursorAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.v(TAG, "On Loader Reset");
        runListView.setAdapter(null);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int id);
    }

    static class RunListCursorLoader extends SQLiteCursorLoader{

        public RunListCursorLoader(Context context) {
            super(context);
            Log.v(TAG, "RunListCursorLoader  started");
        }

        @Override
        protected Cursor loadCursor() {
            //Add code to get the Cursor
            return null;
        }
    }
}