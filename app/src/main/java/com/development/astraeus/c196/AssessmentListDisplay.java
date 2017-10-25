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

public class AssessmentListDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list_display);

        setHandlers();
    }

    private void setHandlers() {
        ListView coursesList = (ListView) findViewById(R.id.assessmentsList);
        coursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) parent.getItemAtPosition(position);
                int assessmentId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Assessments._ID));
                int courseId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_COURSE_ID));
                String title = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_TITLE));
                long due = item.getLong(item.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_DUE));
                String type = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Assessments.COLUMN_TYPE));

                Intent intent = new Intent(AssessmentListDisplay.this, AssessmentDetailDisplay.class);
                intent.putExtra("assessmentId", assessmentId);
                intent.putExtra("courseId", courseId);
                intent.putExtra("title", title);
                intent.putExtra("type", type);
                intent.putExtra("due", due);
                startActivityForResult(intent, 0);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addAssessmentButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AssessmentListDisplay.this, AssessmentDetailDisplay.class), 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(DatabaseContract.SELECT_ALL_ASSESSMENTS_ORDERED_BY_COURSE, new String[]{});

        ListView assessmentList = (ListView) findViewById(R.id.assessmentsList);
        AssessmentListAdapter adapter = (AssessmentListAdapter) assessmentList.getAdapter();
        if(adapter == null){
            assessmentList.setAdapter(new AssessmentListAdapter(this, cursor));
        } else {
            adapter.swapCursor(cursor);
        }
    }
}
