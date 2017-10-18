package com.development.astraeus.c196;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TermDetailDisplay extends AppCompatActivity {

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

        int termId = getIntent().getIntExtra("termId", -1);
        if(termId != -1){
            EditText titleField = (EditText) findViewById(R.id.termTitleField);
            titleField.setText(getIntent().getStringExtra("title"));
            EditText startField = (EditText) findViewById(R.id.termStartField);
            EditText endField = (EditText) findViewById(R.id.termEndField);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(getIntent().getLongExtra("start", 0));
            startField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));

            calendar.setTimeInMillis(getIntent().getLongExtra("end", 0));
            endField.setText(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void saveToDB() {
        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        EditText titleField = (EditText) findViewById(R.id.termTitleField);
        values.put(DatabaseContract.Terms.COLUMN_TITLE, titleField.getText().toString());
        EditText startField = (EditText) findViewById(R.id.termStartField);
        EditText endField = (EditText) findViewById(R.id.termEndField);
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
        db.insert(DatabaseContract.Terms.TABLE_NAME, null, values);
    }
}
