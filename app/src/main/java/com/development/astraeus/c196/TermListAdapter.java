package com.development.astraeus.c196;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Astraeus on 10/17/2017.
 */

public class TermListAdapter extends CursorAdapter {
    public TermListAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.term_list_adapter_layout, parent, false);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleLabel = (TextView) view.findViewById(R.id.titleLabel);
        TextView startLabel = (TextView) view.findViewById(R.id.startLabel);
        TextView endLabel = (TextView) view.findViewById(R.id.endLabel);

        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Terms.COLUMN_TITLE));
        titleLabel.setText(title);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Terms.COLUMN_START)));
        startLabel.setText(new SimpleDateFormat("dd-mm-yyyy").format(calendar.getTime()));

        calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Terms.COLUMN_END)));
        endLabel.setText(new SimpleDateFormat("dd-mm-yyyy").format(calendar.getTime()));
    }
}
