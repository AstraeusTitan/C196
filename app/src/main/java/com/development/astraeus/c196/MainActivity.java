package com.development.astraeus.c196;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        deleteDatabase(DatabaseHelper.DATABASE_NAME);

        Button termsButton = (Button) findViewById(R.id.termsButton);
        termsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.development.astraeus.c196.TermListDisplay.class));
            }
        });

        Button coursesButton = (Button) findViewById(R.id.coursesButton);
        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.development.astraeus.c196.CourseListDisplay.class));
            }
        });

        Button assessmentsButton = (Button) findViewById(R.id.assessmentsButton);
        assessmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.development.astraeus.c196.AssessmentListDisplay.class));
            }
        });
    }
}
