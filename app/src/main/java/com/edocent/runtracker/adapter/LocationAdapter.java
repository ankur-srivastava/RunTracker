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
public class LocationAdapter extends CursorAdapter {

    private static final String TAG = "LocationAdapter";
    private LayoutInflater cursorInflater;

    public LocationAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.list_item_location, parent, false);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        TextView displayLatitudeId = (TextView) convertView.findViewById(R.id.displayLatitudeId);
        TextView displayLongitudeId = (TextView) convertView.findViewById(R.id.displayLongitudeId);
        TextView displayAltitudeId = (TextView) convertView.findViewById(R.id.displayAltitudeId);

        Log.v(TAG, "Check ID "+cursor.getString(0));
        displayLatitudeId.setText(cursor.getString(1));
        displayLongitudeId.setText(cursor.getString(2));
        displayAltitudeId.setText(cursor.getString(3));
    }
}
