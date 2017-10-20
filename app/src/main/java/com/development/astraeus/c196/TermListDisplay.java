package com.development.astraeus.c196;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
        termsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) parent.getItemAtPosition(position);
                int itemId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Terms._ID));
                String title = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Terms.COLUMN_TITLE));
                long start = item.getLong(item.getColumnIndexOrThrow(DatabaseContract.Terms.COLUMN_START));
                long end = item.getLong(item.getColumnIndexOrThrow(DatabaseContract.Terms.COLUMN_END));

                Intent intent = new Intent(TermListDisplay.this, TermDetailDisplay.class);
                intent.putExtra("termId", itemId);
                intent.putExtra("title", title);
                intent.putExtra("start", start);
                intent.putExtra("end", end);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addTermButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermListDisplay.this, TermDetailDisplay.class));
            }
        });
    }
}
