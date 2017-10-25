package com.development.astraeus.c196;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CourseDetailDisplay extends AppCompatActivity {
    private boolean updateCourse = false;
    private static List<String> statuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_display);

        int courseId = getIntent().getIntExtra("courseId", -1);
        if(courseId != -1){
            updateCourse = true;
        }

        setHandlers();
        setSpinners();
        setAutocomplete();
        setFields();
    }

    private void setHandlers() {
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

        Button addAssessmentButton = (Button) findViewById(R.id.addAssessmentButton);
        final DatabaseHelper dbHelper = new DatabaseHelper(this);
        final EditText titleField = (EditText) findViewById(R.id.courseTitleField);
        addAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!updateCourse){
                    saveToDB();
                    updateCourse = true;
                }
                Intent intent = new Intent(CourseDetailDisplay.this, AssessmentDetailDisplay.class);
                intent.putExtra("courseId", dbHelper.getCourseIdFromTitle(titleField.getText().toString()));
                getIntent().putExtra("courseId", dbHelper.getCourseIdFromTitle(titleField.getText().toString()));
                startActivity(intent);
            }
        });

        ListView assessmentList = (ListView) findViewById(R.id.assessmentsList);
        assessmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) parent.getItemAtPosition(position);
                int assessmentId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Assessments._ID));
                int courseId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_COURSE_ID));
                String title = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_TITLE));
                String type = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_TYPE));
                long due = item.getLong(item.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_DUE));

                Intent intent = new Intent(CourseDetailDisplay.this, AssessmentDetailDisplay.class);
                intent.putExtra("assessmentId", assessmentId);
                intent.putExtra("courseId", courseId);
                intent.putExtra("title", title);
                intent.putExtra("type", type);
                intent.putExtra("due", due);
                startActivity(intent);
            }
        });

        Button addNoteButton = (Button) findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!updateCourse){
                    saveToDB();
                    updateCourse = true;
                }
                Intent intent = new Intent(CourseDetailDisplay.this, NoteDetailDisplay.class);
                intent.putExtra("courseId", dbHelper.getCourseIdFromTitle(titleField.getText().toString()));
                getIntent().putExtra("courseId", dbHelper.getCourseIdFromTitle(titleField.getText().toString()));
                startActivity(intent);
            }
        });

        ListView notesList = (ListView) findViewById(R.id.notesList);
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) parent.getItemAtPosition(position);
                int noteId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Notes._ID));
                int courseId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Notes.COLUMN_COURSE_ID));
                String content = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Notes.COLUMN_CONTENT));

                Intent intent = new Intent(CourseDetailDisplay.this, NoteDetailDisplay.class);
                intent.putExtra("noteId", noteId);
                intent.putExtra("courseId", courseId);
                intent.putExtra("content", content);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void setFields() {
        int courseId = getIntent().getIntExtra("courseId", -1);
        if(courseId != -1){
            updateCourse = true;
            EditText titleField = (EditText) findViewById(R.id.courseTitleField);
            titleField.setText(getIntent().getStringExtra("title"));

            TextView idLabel = (TextView) findViewById(R.id.courseIdLabel);
            idLabel.setText(getIntent().getStringExtra("courseId"));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(getIntent().getLongExtra("start", 0));
            TextView startField = (TextView) findViewById(R.id.courseStartField);
            startField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

            calendar.setTimeInMillis(getIntent().getLongExtra("expectedEnd", 0));
            TextView expectedEndField = (TextView) findViewById(R.id.courseExpectedEnd);
            expectedEndField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

            AutoCompleteTextView mentorField = (AutoCompleteTextView) findViewById(R.id.mentorField);
            mentorField.setText(getIntent().getStringExtra("mentorName"));

            EditText phoneNumberField = (EditText) findViewById(R.id.phoneNumberField);
            phoneNumberField.setText(getIntent().getStringExtra("phoneNumber"));

            EditText emailField = (EditText) findViewById(R.id.emailField);
            emailField.setText(getIntent().getStringExtra("email"));
        }
    }

    private void updateAssessmentsList() {
        if(updateCourse){
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            int courseId = getIntent().getIntExtra("courseId", -1);
            Cursor cursor = db.rawQuery(DatabaseContract.SELECT_ALL_ASSESSMENTS_FOR_COURSE, new String[]{"" + courseId});

            ListView assessmentList = (ListView) findViewById(R.id.assessmentsList);
            AssessmentListAdapter adapter = (AssessmentListAdapter) assessmentList.getAdapter();
            if(adapter == null){
                adapter = new AssessmentListAdapter(this, cursor);
                assessmentList.setAdapter(adapter);
            } else {
                adapter.swapCursor(cursor);
            }
            adapter.setSubHeaderVisibility(false);
            setAssessmentListHeight(adapter);
        }
    }

    private void setAssessmentListHeight(AssessmentListAdapter listAdapter) {
        ListView assessmentList = (ListView) findViewById(R.id.assessmentsList);
        int listHeight = 0;
        for(int i = 0; i < listAdapter.getCount(); i++){
            View item = listAdapter.getView(i, null, assessmentList);
            item.measure(0, 0);
            listHeight += item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams assessmentListLayoutParams = assessmentList.getLayoutParams();
        assessmentListLayoutParams.height = listHeight;
        assessmentList.setLayoutParams(assessmentListLayoutParams);
    }

    private void updateNotesList(){
        if(updateCourse){
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            int courseId = getIntent().getIntExtra("courseId", -1);
            Cursor cursor = db.rawQuery(DatabaseContract.SELECT_ALL_NOTES_FOR_COURSE, new String[]{"" + courseId});

            ListView notesList = (ListView) findViewById(R.id.notesList);
            NoteListAdapter adapter = (NoteListAdapter) notesList.getAdapter();
            if(adapter == null){
                adapter = new NoteListAdapter(this, cursor);
                notesList.setAdapter(adapter);
            } else {
                adapter.swapCursor(cursor);
            }
            setNoteListHeight(adapter);
        }
    }

    private void setNoteListHeight(NoteListAdapter listAdapter) {
        ListView notesList = (ListView) findViewById(R.id.notesList);
        int listHeight = 0;
        for(int i = 0; i < listAdapter.getCount(); i++){
            View item = listAdapter.getView(i, null, notesList);
            item.measure(0, 0);
            listHeight += item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams notesListLayoutParams = notesList.getLayoutParams();
        notesListLayoutParams.height = listHeight;
        notesList.setLayoutParams(notesListLayoutParams);
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

        if(statuses == null){
            statuses = new ArrayList<>();
            statuses.add("Not Started");
            statuses.add("In Progress");
            statuses.add("Completed");
        }
        statusSelector.setAdapter(new ArrayAdapter<>(this, android.R.layout.select_dialog_item, statuses));
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
        } catch (ParseException ignored) {
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            courseValues.put(DatabaseContract.Courses.COLUMN_START, now);
        }
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(expectedEndField.getText().toString()));
            long endDate = calendar.getTimeInMillis();
            courseValues.put(DatabaseContract.Courses.COLUMN_EXPECTED_END, endDate);
        } catch (ParseException ignored) {
            Calendar calendar = Calendar.getInstance();
            long now = calendar.getTimeInMillis();
            courseValues.put(DatabaseContract.Courses.COLUMN_EXPECTED_END, now);
        }
        if(updateCourse){
            db.update(DatabaseContract.Courses.TABLE_NAME, courseValues, DatabaseContract.Courses._ID + "=?",
                    new String[]{"" + getIntent().getIntExtra("courseId", -1)});
        } else {
            db.insert(DatabaseContract.Courses.TABLE_NAME, null, courseValues);
        }
    }

    private void deleteFromDB() {
        if(updateCourse){
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.delete(DatabaseContract.Courses.TABLE_NAME, DatabaseContract.Courses._ID + "=?", new String[]{"" + getIntent().getIntExtra("courseId", -1)});
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

    @Override
    protected void onResume() {
        super.onResume();
        updateAssessmentsList();
        updateNotesList();
    }
}
