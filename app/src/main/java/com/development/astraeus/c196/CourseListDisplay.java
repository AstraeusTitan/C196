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

public class CourseListDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list_display);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(DatabaseContract.SELECT_ALL_COURSES_ORDERED_BY_TERM, new String[] {});

        ListView coursesList = (ListView) findViewById(R.id.coursesList);
        CourseListAdapter adapter = new CourseListAdapter(this, cursor);
        coursesList.setAdapter(adapter);
        coursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) parent.getItemAtPosition(position);
                int courseId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Courses._ID));
                int termId = item.getInt(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_TERM_ID));
                String title = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_TITLE));
                long start = item.getLong(item.getColumnIndexOrThrow(DatabaseContract.Terms.COLUMN_START));
                long expectedEnd = item.getLong(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_EXPECTED_END));
                String mentorName = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_MENTOR_NAME));
                String phoneNumber = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_MENTOR_PHONE_NUMBER));
                String email = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_MENTOR_EMAIL));
                String status = item.getString(item.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_STATUS));

                Intent intent = new Intent(CourseListDisplay.this, CourseDetailDisplay.class);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addCourseButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseListDisplay.this, CourseDetailDisplay.class));
            }
        });
    }
}
