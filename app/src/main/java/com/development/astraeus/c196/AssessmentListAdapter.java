package com.development.astraeus.c196;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Astraeus on 10/21/2017.
 */

public class AssessmentListAdapter extends CursorAdapter {
    private int currentCourseId = -1;
    private boolean subHeaderVisibility = true;
    public AssessmentListAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.assessment_list_adapter_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView subHeader = (TextView) view.findViewById(R.id.subHeader);
        TextView titleLabel = (TextView) view.findViewById(R.id.assessmentTitleLabel);
        TextView dueDateLabel = (TextView) view.findViewById(R.id.dueDateLabel);

        String assessmentTitle = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_TITLE));
        titleLabel.setText(assessmentTitle);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_DUE)));
        dueDateLabel.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

        int courseId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_COURSE_ID));
        if(subHeaderVisibility){
            if(courseId != currentCourseId){
                currentCourseId = courseId;
                subHeader.setVisibility(View.VISIBLE);
                if(courseId < 1){
                    subHeader.setText("No Course");
                } else {
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    subHeader.setText(dbHelper.getCourseTitleFromId(currentCourseId));
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
        currentCourseId = -1;
        return super.swapCursor(newCursor);
    }

    public void setSubHeaderVisibility(boolean tf){
        subHeaderVisibility = tf;
    }
}
