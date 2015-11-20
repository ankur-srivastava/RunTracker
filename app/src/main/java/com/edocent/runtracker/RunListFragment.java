package com.edocent.runtracker;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
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

/**
 * @author Ankur
 */
public class RunListFragment extends ListFragment {

    static final int REQUEST_NEW_RUN = 0;
    static final String TAG = "RunListFragment";

    ListView runListView;
    OnFragmentInteractionListener mListener;
    RunDatabaseHelper helper;
    CursorAdapter cursorAdapter;
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
        cursorAdapter = new RunAdapter(getActivity(), RunDatabaseHelper.getRuns(helper, tempCursor), 0);

        runListView = (ListView) view.findViewById(R.id.runListViewId);
        if(cursorAdapter != null){
            Log.v(TAG, "Set Cursor Adapter");
            runListView.setAdapter(cursorAdapter);
        }

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int id);
    }
}