package com.development.astraeus.c196;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        Button button = (Button) findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDB();
                startActivity(new Intent(TermDetailDisplay.this, TermListDisplay.class));
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

        int termId = getIntent().getIntExtra("termId", -1);
        if(termId != -1){
            updateTerm = true;
            EditText titleField = (EditText) findViewById(R.id.termTitleField);
            titleField.setText(getIntent().getStringExtra("title"));

            TextView termIdLabel = (TextView) findViewById(R.id.termIdLabel);
            termIdLabel.setText("#" + termId);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(getIntent().getLongExtra("start", 0));
            startField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

            calendar.setTimeInMillis(getIntent().getLongExtra("end", 0));
            endField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
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

    @SuppressLint("SimpleDateFormat")
    private void saveToDB() {
        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        EditText titleField = (EditText) findViewById(R.id.termTitleField);
        values.put(DatabaseContract.Terms.COLUMN_TITLE, titleField.getText().toString());
        TextView startField = (TextView) findViewById(R.id.termStartField);
        TextView endField = (TextView) findViewById(R.id.termEndField);
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(startField.getText().toString()));
            long startDate = calendar.getTimeInMillis();
            values.put(DatabaseContract.Terms.COLUMN_START, startDate);
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(endField.getText().toString()));
            long endDate = calendar.getTimeInMillis();
            values.put(DatabaseContract.Terms.COLUMN_END, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(updateTerm){
            db.update(DatabaseContract.Terms.TABLE_NAME, values, DatabaseContract.Terms._ID + "=?",
                    new String[]{"" + getIntent().getIntExtra("termId", -1)});
        } else {
            db.insert(DatabaseContract.Terms.TABLE_NAME, null, values);
        }
    }
}
