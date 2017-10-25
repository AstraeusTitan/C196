package com.development.astraeus.c196;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Astraeus on 10/24/2017.
 */

public class NoteListAdapter extends CursorAdapter {
    public NoteListAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_list_adapter_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView content = (TextView) view.findViewById(R.id.noteContent);
        content.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Notes.COLUMN_CONTENT)));
    }
}
