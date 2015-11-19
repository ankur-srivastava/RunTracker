package com.edocent.runtracker;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.edocent.runtracker.adapter.RunAdapter;
import com.edocent.runtracker.database.RunDatabaseHelper;
import com.edocent.runtracker.dummy.DummyContent;

/**
 * @author Ankur
 */
public class RunListFragment extends ListFragment {

    ListView runListView;
    OnFragmentInteractionListener mListener;
    RunDatabaseHelper helper;
    CursorAdapter cursorAdapter;

    public RunListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new RunDatabaseHelper(getActivity());
        cursorAdapter = new RunAdapter(getActivity(), RunDatabaseHelper.getRuns(helper), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.run_list_fragment, container, false);

        runListView = (ListView) getActivity().findViewById(R.id.runListViewId);
        runListView.setAdapter(cursorAdapter);

        return view;
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
        mListener = null;
        helper = null;
        cursorAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListener = null;
        helper = null;
        cursorAdapter = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (null != mListener) {
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String id);
    }
}