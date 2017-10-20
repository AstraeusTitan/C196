package com.development.astraeus.c196;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CourseDetailDisplay extends AppCompatActivity {
    private boolean updateCourse = false;
    private final List<String> statuses = new ArrayList<>();

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_display);

        statuses.add("Not Started");
        statuses.add("In Progress");
        statuses.add("Completed");

        TextView startField = (TextView) findViewById(R.id.courseStartField);
        startField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        TextView expectedEndField = (TextView) findViewById(R.id.courseExpectedEnd);
        expectedEndField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });

        Button button = (Button) findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB();
                startActivity(new Intent(CourseDetailDisplay.this, CourseListDisplay.class));
            }
        });

        int courseId = getIntent().getIntExtra("courseId", -1);
        if(courseId != -1){
            updateCourse = true;
            EditText titleField = (EditText) findViewById(R.id.courseTitleField);
            titleField.setText(getIntent().getStringExtra("title"));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(getIntent().getLongExtra("start", 0));
            startField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

            calendar.setTimeInMillis(getIntent().getLongExtra("expectedEnd", 0));
            expectedEndField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

            AutoCompleteTextView mentorField = (AutoCompleteTextView) findViewById(R.id.mentorField);
            mentorField.setText(getIntent().getStringExtra("mentorName"));

            EditText phoneNumberField = (EditText) findViewById(R.id.phoneNumberField);
            phoneNumberField.setText(getIntent().getStringExtra("phoneNumber"));

            EditText emailField = (EditText) findViewById(R.id.emailField);
            emailField.setText(getIntent().getStringExtra("email"));
        }
        setSpinners();
        setAutocomplete();
    }

    private void setAutocomplete() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        AutoCompleteTextView mentorField = (AutoCompleteTextView) findViewById(R.id.mentorField);
        ArrayAdapter<String> mentorNamesAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, dbHelper.getMentorNames());
        mentorField.setAdapter(mentorNamesAdapter);
        mentorField.setThreshold(1);
    }

    private void setSpinners() {
        Spinner statusSelector = (Spinner) findViewById(R.id.statusSelector);
        Spinner termSelector = (Spinner) findViewById(R.id.termSelector);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        ArrayAdapter<String> statusesAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, statuses);
        statusSelector.setAdapter(statusesAdapter);
        statusSelector.setSelection(statuses.indexOf(getIntent().getStringExtra("status")));


        List<String> termTitles = new ArrayList<>();
        termTitles.add("");
        termTitles.addAll(dbHelper.getTermTitles());
        ArrayAdapter<String> termTitlesAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, termTitles);
        termSelector.setAdapter(termTitlesAdapter);
        int termId = getIntent().getIntExtra("termId", -1);
        if(termId != -1){
            String courseTerm = dbHelper.getTermTitleFromId(termId);
            termSelector.setSelection(termTitles.indexOf(courseTerm));
        } else {
            termSelector.setSelection(termTitles.indexOf(""));
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void saveToDB() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues courseValues = new ContentValues();
        EditText titleField = (EditText) findViewById(R.id.courseTitleField);
        Spinner termSelector = (Spinner) findViewById(R.id.termSelector);
        Spinner statusSelector = (Spinner) findViewById(R.id.statusSelector);
        TextView startField = (TextView) findViewById(R.id.courseStartField);
        TextView expectedEndField = (TextView) findViewById(R.id.courseExpectedEnd);
        AutoCompleteTextView mentorField = (AutoCompleteTextView) findViewById(R.id.mentorField);
        EditText phoneNumberField = (EditText) findViewById(R.id.phoneNumberField);
        EditText emailField = (EditText) findViewById(R.id.emailField);

        courseValues.put(DatabaseContract.Courses.COLUMN_TITLE, titleField.getText().toString());
        courseValues.put(DatabaseContract.Courses.COLUMN_TERM_ID, dbHelper.getTermIdFromTitle((String)termSelector.getSelectedItem()));
        courseValues.put(DatabaseContract.Courses.COLUMN_MENTOR_NAME, mentorField.getText().toString());
        courseValues.put(DatabaseContract.Courses.COLUMN_MENTOR_PHONE_NUMBER, phoneNumberField.getText().toString());
        courseValues.put(DatabaseContract.Courses.COLUMN_MENTOR_EMAIL, emailField.getText().toString());
        courseValues.put(DatabaseContract.Courses.COLUMN_STATUS, (String) statusSelector.getSelectedItem());
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(startField.getText().toString()));
            long startDate = calendar.getTimeInMillis();
            courseValues.put(DatabaseContract.Courses.COLUMN_START, startDate);
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(expectedEndField.getText().toString()));
            long endDate = calendar.getTimeInMillis();
            courseValues.put(DatabaseContract.Courses.COLUMN_EXPECTED_END, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(updateCourse){
            db.update(DatabaseContract.Courses.TABLE_NAME, courseValues, DatabaseContract.Terms._ID + "=?",
                    new String[]{"" + getIntent().getIntExtra("courseId", -1)});
        } else {
            db.insert(DatabaseContract.Courses.TABLE_NAME, null, courseValues);
        }
    }

    private void showDatePicker(View view){
        DatePickerFragment dialog = new DatePickerFragment();
        getIntent().putExtra("callingField", view.getId());
        TextView callingField = (TextView) view;
        String[] datePieces = callingField.getText().toString().split("/");
        getIntent().putExtra("new", !updateCourse);
        if(updateCourse){
            getIntent().putExtra("day", Integer.parseInt(datePieces[0]));
            getIntent().putExtra("month", Integer.parseInt(datePieces[1]) - 1);
            getIntent().putExtra("year", Integer.parseInt(datePieces[2]));
        }
        dialog.show(getFragmentManager(), "datePicker");
    }
}
