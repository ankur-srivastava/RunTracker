package com.edocent.runtracker.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.edocent.runtracker.R;

/**
 * Created by ankursrivastava on 11/19/15.
 */
public class RunAdapter extends CursorAdapter {

    private static final String TAG = "RunAdapter";
    private LayoutInflater cursorInflater;

    public RunAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.list_item_run, parent, false);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        TextView runIdView = (TextView) convertView.findViewById(R.id.displayRunId);
        TextView runDateView = (TextView) convertView.findViewById(R.id.displayRunDate);
        Log.v(TAG, "Check the Run ID "+cursor.getString(0));
        runIdView.setText(cursor.getString(0));
        runDateView.setText(cursor.getString(1));
    }
}
