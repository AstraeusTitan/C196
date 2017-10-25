package com.development.astraeus.c196;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.ShareActionProvider;

public class NoteDetailDisplay extends AppCompatActivity {
    private boolean updateNote = false;
    private ShareActionProvider mShareActionProvider;
    private Intent mShareIntent = new Intent(Intent.ACTION_SEND);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail_display);

        setHandlers();
        setFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateShareIntent();
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(mShareIntent);
        return true;
    }

    private Intent updateShareIntent() {
        mShareIntent.setType("text/plain");
        String subject = "Note from Course #" + getIntent().getIntExtra("courseId", -1);
        mShareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        EditText contentField = (EditText) findViewById(R.id.contentField);
        String content = contentField.getText().toString();
        mShareIntent.putExtra(Intent.EXTRA_TEXT, content);
        return mShareIntent;
    }



    private void setFields() {
        int noteId = getIntent().getIntExtra("noteId", -1);
        if(noteId != -1){
            updateNote = true;
            EditText contentField = (EditText) findViewById(R.id.contentField);
            contentField.setText(getIntent().getStringExtra("content"));
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

        EditText contentField = (EditText) findViewById(R.id.contentField);
        contentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateShareIntent();
            }
        });
    }

    private void saveToDB() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues noteValues = new ContentValues();

        noteValues.put(DatabaseContract.Notes.COLUMN_COURSE_ID, getIntent().getIntExtra("courseId", -1));
        EditText contentField = (EditText) findViewById(R.id.contentField);
        noteValues.put(DatabaseContract.Notes.COLUMN_CONTENT, contentField.getText().toString());

        if(updateNote){
            db.update(DatabaseContract.Notes.TABLE_NAME, noteValues, DatabaseContract.Notes._ID + "=?",
                    new String[]{"" + getIntent().getIntExtra("courseId", -1)});
        } else {
            db.insert(DatabaseContract.Notes.TABLE_NAME, null, noteValues);
        }
    }

    private void deleteFromDB() {
        if(updateNote){
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(DatabaseContract.Notes.TABLE_NAME, DatabaseContract.Notes._ID+ "=?", new String[]{"" + getIntent().getIntExtra("noteId", -1)});
        }
    }
}
