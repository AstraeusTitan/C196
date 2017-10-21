package com.development.astraeus.c196;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
        db.execSQL(DatabaseContract.Courses.CREATE_TABLE);
        db.execSQL(DatabaseContract.Assessments.CREATE_TABLE);
        db.execSQL(DatabaseContract.Notes.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Terms.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Courses.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Assessments.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Notes.TABLE_NAME);

        onCreate(db);
    }

    public List<String> getMentorNames(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mentorsCursor = db.rawQuery(DatabaseContract.SELECT_ALL_MENTOR_NAMES_ORDERED_ALPHABETICALLY, null);
        List<String> mentorNames = new ArrayList<>();
        if(mentorsCursor.moveToFirst()){
            do {
                mentorNames.add(mentorsCursor.getString(mentorsCursor.getColumnIndexOrThrow(DatabaseContract.Courses.COLUMN_MENTOR_NAME)));
            } while (mentorsCursor.moveToNext());
        }
        mentorsCursor.close();
        return mentorNames;
    }

    public List<String> getTermTitles(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor termsCursor = db.rawQuery(DatabaseContract.SELECT_ALL_TERM_TITLES_ORDERED_BY_START, null);
        List<String> termTitles = new ArrayList<>();
        if(termsCursor.moveToFirst()){
            do {
                termTitles.add(termsCursor.getString(termsCursor.getColumnIndexOrThrow(DatabaseContract.Terms.COLUMN_TITLE)));
            } while (termsCursor.moveToNext());
        }
        termsCursor.close();
        return termTitles;
    }

    public String getTermTitleFromId(int termId){
        if(termId < 1){
            return "";
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor termsCursor = db.rawQuery(DatabaseContract.SELECT_TERM_BY_ID, new String[]{"" + termId});
        termsCursor.moveToFirst();
        String termTitle = termsCursor.getString(termsCursor.getColumnIndexOrThrow(DatabaseContract.Terms.COLUMN_TITLE));
        termsCursor.close();
        return termTitle;
    }

    public int getTermIdFromTitle(String termTitle){
        if(termTitle.equals("")){
            return -1;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor termsCursor = db.rawQuery(DatabaseContract.SELECT_TERM_BY_TITLE, new String[]{termTitle});
        termsCursor.moveToFirst();
        int termId = termsCursor.getInt(termsCursor.getColumnIndexOrThrow(DatabaseContract.Terms._ID));
        termsCursor.close();
        return termId;
    }
}
