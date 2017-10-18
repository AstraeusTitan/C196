package com.development.astraeus.c196;

import android.provider.BaseColumns;

/**
 * Created by Astraeus on 10/15/2017.
 */

class DatabaseContract {
    //Store used queries as final strings here
    static final String SELECT_ALL_TERMS = "SELECT * FROM " + Terms.TABLE_NAME;

    private DatabaseContract(){}

    static class Terms implements BaseColumns{
        static final String TABLE_NAME = "terms";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_START = "start";
        static final String COLUMN_END = "end";
        static final String COLUMN_CURRENT = "current";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_START + " INTEGER, " +
                COLUMN_END + " INTEGER, " +
                COLUMN_CURRENT + " INTEGER"+ ")";
    }

    static class Mentors implements BaseColumns{
        static final String TABLE_NAME = "mentors";
        static final String COLUMN_NAME = "name";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT" + ")";
    }

    static class PhoneNumbers implements BaseColumns{
        static final String TABLE_NAME = "phoneNumbers";
        static final String COLUMN_MENTOR_ID = "mentorId";
        static final String COLUMN_NUMBER = "number";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENTOR_ID + " INTEGER, " +
                COLUMN_NUMBER + " TEXT" + ")";
    }

    static class Emails implements BaseColumns{
        static final String TABLE_NAME = "emails";
        static final String COLUMN_MENTOR_ID = "mentorId";
        static final String COLUMN_ADDRESS = "address";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENTOR_ID + " INTEGER, " +
                COLUMN_ADDRESS + " TEXT" + ")";
    }

    static class Courses implements BaseColumns{
        static final String TABLE_NAME = "courses";
        static final String COLUMN_TERM_ID = "termId";
        static final String COLUMN_MENTOR_ID = "mentorId";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_START = "start";
        static final String COLUMN_EXPECTED_END = "expectedEnd";
        static final String COLUMN_STATUS = "status";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TERM_ID + " INTEGER, " +
                COLUMN_MENTOR_ID + " INTEGER, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_START + " INTEGER, " +
                COLUMN_EXPECTED_END + " INTEGER, " +
                COLUMN_STATUS + " TEXT" + ")";
    }

    static class Assessments implements  BaseColumns{
        static final String TABLE_NAME = "assessments";
        static final String COLUMN_COURSE_ID = "courseId";
        static final String COLUMN_TITLE = "title";
        static final String COLUMN_DUE = "due";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_ID + " INTEGER, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DUE + " INTEGER" + ")";
    }

    static class Notes implements BaseColumns{
        static final String TABLE_NAME = "notes";
        static final String COLUMN_ASSESSMENT_ID = "assessmentId";
        static final String COLUMN_CONTENT = "content";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ASSESSMENT_ID + " INTEGER, " +
                COLUMN_CONTENT + " TEXT" + ")";
    }
}
