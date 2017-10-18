package com.development.astraeus.c196;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.development.astraeus.c196.DatabaseContract;

/**
 * Created by Astraeus on 10/15/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "storage_database";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.Terms.CREATE_TABLE);
        db.execSQL(DatabaseContract.Mentors.CREATE_TABLE);
        db.execSQL(DatabaseContract.PhoneNumbers.CREATE_TABLE);
        db.execSQL(DatabaseContract.Emails.CREATE_TABLE);
        db.execSQL(DatabaseContract.Courses.CREATE_TABLE);
        db.execSQL(DatabaseContract.Assessments.CREATE_TABLE);
        db.execSQL(DatabaseContract.Notes.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Terms.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Mentors.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.PhoneNumbers.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Emails.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Courses.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Assessments.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Notes.TABLE_NAME);

        onCreate(db);
    }
}
