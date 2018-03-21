package com.development.astraeus.c196;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AssessmentDetailDisplay extends AppCompatActivity {
    private boolean updateAssessment = false;
    private static List<String> types;
    private SharedPreferences mSharedPreferences;
    public static final String DUE_REMINDER_NAME = "dueDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail_display);

        mSharedPreferences = getPreferences(0);

        setHandlers();
        setSpinners();
        setFields();
    }

    @SuppressLint("SimpleDateFormat")
    private void setFields() {
        int assessmentId = getIntent().getIntExtra("assessmentId", -1);
        if(assessmentId != -1){
            updateAssessment = true;
            EditText titleField = (EditText) findViewById(R.id.assessmentTitleField);
            titleField.setText(getIntent().getStringExtra("title"));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(getIntent().getLongExtra("due", 0));
            TextView startField = (TextView) findViewById(R.id.dueDateField);
            startField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
        }
    }

    private void setHandlers() {
        final TextView dueDateField = (TextView) findViewById(R.id.dueDateField);
        dueDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
        dueDateField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                long date = parseDateField(dueDateField);
                updateReminder(DUE_REMINDER_NAME, date);
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB();
                setResult(0);
                finish();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDB();
                setResult(0);
                finish();
            }
        });
    }

    private void setSpinners() {
        Spinner courseSelector = (Spinner) findViewById(R.id.courseSelector);
        Spinner typeSelector = (Spinner) findViewById(R.id.typeSelector);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        if(types == null){
            types = new ArrayList<>();
            types.add("Objective");
            types.add("Performance");
        }
        typeSelector.setAdapter(new ArrayAdapter<>(this, android.R.layout.select_dialog_item, types));
        typeSelector.setSelection(types.indexOf(getIntent().getStringExtra("type")));


        List<String> courseTitles = new ArrayList<>();
        courseTitles.add("");
        courseTitles.addAll(dbHelper.getCourseTitles());
        ArrayAdapter<String> courseTitleSelector = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, courseTitles);
        courseSelector.setAdapter(courseTitleSelector);
        int courseId = getIntent().getIntExtra("courseId", -1);
        if(courseId != -1){
            String courseTerm = dbHelper.getCourseTitleFromId(courseId);
            courseSelector.setSelection(courseTitles.indexOf(courseTerm));
        } else {
            courseSelector.setSelection(courseTitles.indexOf(""));
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void saveToDB() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues assessmentValues = new ContentValues();

        EditText titleField = (EditText) findViewById(R.id.assessmentTitleField);
        Spinner courseSelector = (Spinner) findViewById(R.id.courseSelector);
        Spinner typeSelector = (Spinner) findViewById(R.id.typeSelector);
        TextView dueDateField = (TextView) findViewById(R.id.dueDateField);

        assessmentValues.put(DatabaseContract.Assessments.COLUMN_TITLE, titleField.getText().toString());
        int courseId = dbHelper.getCourseIdFromTitle((String) courseSelector.getSelectedItem());
        assessmentValues.put(DatabaseContract.Assessments.COLUMN_COURSE_ID, courseId);
        assessmentValues.put(DatabaseContract.Assessments.COLUMN_TYPE, (String) typeSelector.getSelectedItem());
        assessmentValues.put(DatabaseContract.Assessments.COLUMN_DUE, parseDateField(dueDateField));

        if(updateAssessment){
            db.update(DatabaseContract.Assessments.TABLE_NAME, assessmentValues, DatabaseContract.Assessments._ID + "=?",
                    new String[]{"" + getIntent().getIntExtra("assessmentId", -1)});
        } else {
            db.insert(DatabaseContract.Assessments.TABLE_NAME, null, assessmentValues);
        }
    }

    private void deleteFromDB(){
        if(updateAssessment){
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(DatabaseContract.Assessments.TABLE_NAME, DatabaseContract.Assessments._ID + "=?", new String[]{"" + getIntent().getIntExtra("assessmentId", -1)});
        }
    }

    private void updateReminder(String reminderName, long date){
        boolean set = mSharedPreferences.getBoolean(reminderName + "Set", false);
        CheckBox checkBox = (CheckBox) findViewById(R.id.dueReminderToggle);
        boolean toggle = checkBox.isChecked();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if(toggle){
            if(set){
                removeReminder(reminderName, mSharedPreferences.getLong(reminderName, 0));
            }
            setReminder(reminderName, date);
            editor.putBoolean(reminderName + "Set", true);
            editor.putLong(reminderName, date);
        } else {
            if(set){
                removeReminder(reminderName, mSharedPreferences.getLong(reminderName, 0));
            }
            editor.putBoolean(reminderName + "Set", false);
            editor.putLong(reminderName, 0);
        }
        editor.apply();
    }

    private void setReminder(String reminderName, long date){
        long reminderAmount = 24*60*60*1000;    //24 hours in millis
        long time = date - reminderAmount;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, createAlarmIntent(reminderName, date));
    }

    private void removeReminder(String reminderName, long date){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createAlarmIntent(reminderName, date));
    }

    private PendingIntent createAlarmIntent(String reminderName, long date){
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("reminderName", reminderName);
        intent.putExtra("date", date);
        EditText titleField = (EditText) findViewById(R.id.assessmentTitleField);
        String title = titleField.getText().toString();
        intent.putExtra("content", title);
        return PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @SuppressLint("SimpleDateFormat")
    private long parseDateField(TextView field){
        long out;
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(field.getText().toString()));
            out = calendar.getTimeInMillis();
        } catch (ParseException ignored) {
            Calendar calendar = Calendar.getInstance();
            out = calendar.getTimeInMillis();
        }
        return out;
    }

    private void showDatePicker(View view){
        DatePickerFragment dialog = new DatePickerFragment();
        getIntent().putExtra("callingField", view.getId());
        TextView callingField = (TextView) view;
        String[] datePieces = callingField.getText().toString().split("/");
        getIntent().putExtra("new", !updateAssessment);
        if(updateAssessment){
            getIntent().putExtra("day", Integer.parseInt(datePieces[0]));
            getIntent().putExtra("month", Integer.parseInt(datePieces[1]) - 1);
            getIntent().putExtra("year", Integer.parseInt(datePieces[2]));
        }
        dialog.show(getFragmentManager(), "datePicker");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //anything to update on resume
    }
}
