package com.development.astraeus.c196;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class TermListDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list_display);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(DatabaseContract.SELECT_ALL_TERMS, new String[] {});

        ListView termsList = (ListView) findViewById(R.id.termsList);
        TermListAdapter adapter = new TermListAdapter(this, cursor);
        termsList.setAdapter(adapter);
    }
}
