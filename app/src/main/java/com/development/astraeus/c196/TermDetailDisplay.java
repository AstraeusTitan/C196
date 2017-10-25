package com.development.astraeus.c196;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TermDetailDisplay extends AppCompatActivity {
    private boolean updateTerm = false;
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail_display);

        setHandlers();
        setFields();
    }

    @SuppressLint("SimpleDateFormat")
    private void setFields() {
        int termId = getIntent().getIntExtra("termId", -1);
        if(termId != -1){
            updateTerm = true;
            EditText titleField = (EditText) findViewById(R.id.termTitleField);
            titleField.setText(getIntent().getStringExtra("title"));

            TextView termIdLabel = (TextView) findViewById(R.id.termIdLabel);
            termIdLabel.setText("#" + termId);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(getIntent().getLongExtra("start", 0));
            TextView startField = (TextView) findViewById(R.id.termStartField);
            startField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

            calendar.setTimeInMillis(getIntent().getLongExtra("end", 0));
            TextView endField = (TextView) findViewById(R.id.termEndField);
            endField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
        } else {
            TextView termIdLabel = (TextView) findViewById(R.id.termIdLabel);
            termIdLabel.setText("");
        }
    }

    private void setHandlers() {
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB();
                finish();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDB();
                finish();
            }
        });

        TextView startField = (TextView) findViewById(R.id.termStartField);
        startField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        TextView endField = (TextView) findViewById(R.id.termEndField);
        endField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        Button addCourseButton = (Button) findViewById(R.id.addCourseButton);
        final DatabaseHelper dbHelper = new DatabaseHelper(this);
        final EditText titleField = (EditText) findViewById(R.id.termTitleField);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!updateTerm){
                    saveToDB();
                    updateTerm = true;
                }
                Intent intent = new Intent(TermDetailDisplay.this, CourseDetailDisplay.class);
                intent.putExtra("termId", dbHelper.getCourseIdFromTitle(titleField.getText().toString()));
                getIntent().putExtra("termId", dbHelper.getCourseIdFromTitle(titleField.getText().toString()));
                startActivity(intent);
            }
        });

        ListView coursesList = (ListView) findViewById(R.id.coursesList);
        coursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) parent.getItemAtPosition(position);
                int courseId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Courses._ID));
                int termId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_TERM_ID));
                String title = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_TITLE));
                long start = item.getLong(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_START));
                long expectedEnd = item.getLong(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_EXPECTED_END));
                String mentorName = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_MENTOR_NAME));
                String phoneNumber = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_MENTOR_PHONE_NUMBER));
                String email = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_MENTOR_EMAIL));
                String status = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_STATUS));

                Intent intent = new Intent(TermDetailDisplay.this, CourseDetailDisplay.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("termId", termId);
                intent.putExtra("title", title);
                intent.putExtra("start", start);
                intent.putExtra("expectedEnd", expectedEnd);
                intent.putExtra("mentorName", mentorName);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("email", email);
                intent.putExtra("status", status);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void saveToDB() {
        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
        ContentValues termValues = new ContentValues();
        EditText titleField = (EditText) findViewById(R.id.termTitleField);
        termValues.put(DatabaseContract.Terms.COLUMN_TITLE, titleField.getText().toString());
        TextView startField = (TextView) findViewById(R.id.termStartField);
        TextView endField = (TextView) findViewById(R.id.termEndField);
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(startField.getText().toString()));
            long startDate = calendar.getTimeInMillis();
            termValues.put(DatabaseContract.Terms.COLUMN_START, startDate);
        } catch (ParseException ignored) {
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            termValues.put(DatabaseContract.Terms.COLUMN_START, now);
        }
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(endField.getText().toString()));
            long endDate = calendar.getTimeInMillis();
            termValues.put(DatabaseContract.Terms.COLUMN_END, endDate);
        } catch (ParseException ignored) {
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            termValues.put(DatabaseContract.Terms.COLUMN_END, now);
        }

        if(updateTerm){
            db.update(DatabaseContract.Terms.TABLE_NAME, termValues, DatabaseContract.Terms._ID + "=?",
                    new String[]{"" + getIntent().getIntExtra("termId", -1)});
        } else {
            db.insert(DatabaseContract.Terms.TABLE_NAME, null, termValues);
        }
    }

    private void deleteFromDB() {
        if(updateTerm){
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.delete(DatabaseContract.Terms.TABLE_NAME, DatabaseContract.Terms._ID + "=?", new String[]{"" + getIntent().getIntExtra("termId", -1)});
        }
    }

    private void showDatePicker(View view){
        DatePickerFragment dialog = new DatePickerFragment();
        getIntent().putExtra("callingField", view.getId());
        TextView callingField = (TextView) view;
        String[] datePieces = callingField.getText().toString().split("/");
        getIntent().putExtra("new", !updateTerm);
        if(updateTerm){
            getIntent().putExtra("day", Integer.parseInt(datePieces[0]));
            getIntent().putExtra("month", Integer.parseInt(datePieces[1]) - 1);
            getIntent().putExtra("year", Integer.parseInt(datePieces[2]));
        }
        dialog.show(getFragmentManager(), "datePicker");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        if(updateTerm){
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(DatabaseContract.SELECT_ALL_COURSES_BY_TERM, new String[]{"" + getIntent().getIntExtra("termId", -1)});

            ListView courseList = (ListView) findViewById(R.id.coursesList);
            CourseListAdapter adapter = (CourseListAdapter) courseList.getAdapter();
            if(adapter == null){
                adapter = new CourseListAdapter(this, cursor);
                adapter.setSubHeaderVisibility(false);
            } else {
                adapter.swapCursor(cursor);
            }
            courseList.setAdapter(adapter);
            setAssessmentListHeight(adapter);
        }
    }

    private void setAssessmentListHeight(CourseListAdapter listAdapter) {
        ListView coursesList = (ListView) findViewById(R.id.coursesList);
        int listHeight = 0;
        for(int i = 0; i < listAdapter.getCount(); i++){
            View item = listAdapter.getView(i, null, coursesList);
            item.measure(0, 0);
            listHeight += item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams coursesListLayoutParams = coursesList.getLayoutParams();
        coursesListLayoutParams.height = listHeight;
        coursesList.setLayoutParams(coursesListLayoutParams);
    }
}
