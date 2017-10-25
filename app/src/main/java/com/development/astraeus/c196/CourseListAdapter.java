package com.development.astraeus.c196;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Astraeus on 10/18/2017.
 */

class CourseListAdapter extends CursorAdapter {
    private int currentTermId = -1;
    private boolean subHeaderVisibility = true;
    CourseListAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.course_list_adapter_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView subHeader = (TextView) view.findViewById(R.id.subHeader);
        TextView titleLabel = (TextView) view.findViewById(R.id.titleLabel);
        TextView statusLabel = (TextView) view.findViewById(R.id.statusLabel);

        String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_TITLE));
        titleLabel.setText(courseTitle);

        String courseStatus = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_STATUS));
        statusLabel.setText(courseStatus);

        int termId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_TERM_ID));
        if(subHeaderVisibility){
            if(termId != currentTermId){
                currentTermId = termId;
                subHeader.setVisibility(View.VISIBLE);
                if(termId < 1){
                    subHeader.setText("No Term");
                } else {
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    subHeader.setText(dbHelper.getCourseTitleFromId(currentTermId));
                }
            } else {
                subHeader.setVisibility(View.GONE);
            }
        } else {
            subHeader.setVisibility(View.GONE);
        }
    }

    @Override
    public Object getItem(int position){
        return super.getItem(position);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        currentTermId = -1;
        return super.swapCursor(newCursor);
    }

    public void setSubHeaderVisibility(boolean tf){
        subHeaderVisibility = tf;
    }

}
