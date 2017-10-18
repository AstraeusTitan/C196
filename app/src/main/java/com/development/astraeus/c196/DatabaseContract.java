package com.development.astraeus.c196;

import android.provider.BaseColumns;

/**
 * Created by Astraeus on 10/15/2017.
 */

public class DatabaseContract {
    //Store used queries as final strings here
    public static final String SELECT_ALL_TERMS = "SELECT * FROM " + Terms.TABLE_NAME;

    private DatabaseContract(){}

    public static class Terms implements BaseColumns{
        public static final String TABLE_NAME = "terms";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_START = "start";
        public static final String COLUMN_END = "end";
        public static final String COLUMN_CURRENT = "current";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_START + " INTEGER, " +
                COLUMN_END + " INTEGER, " +
                COLUMN_CURRENT + " INTEGER"+ ")";
    }

    public static class Mentors implements BaseColumns{
        public static final String TABLE_NAME = "mentors";
        public static final String COLUMN_NAME = "name";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT" + ")";
    }

    public static class PhoneNumbers implements BaseColumns{
        public static final String TABLE_NAME = "phoneNumbers";
        public static final String COLUMN_MENTOR_ID = "mentorId";
        public static final String COLUMN_NUMBER = "number";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENTOR_ID + " INTEGER, " +
                COLUMN_NUMBER + " TEXT" + ")";
    }

    public static class Emails implements BaseColumns{
        public static final String TABLE_NAME = "emails";
        public static final String COLUMN_MENTOR_ID = "mentorId";
        public static final String COLUMN_ADDRESS = "address";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENTOR_ID + " INTEGER, " +
                COLUMN_ADDRESS + " TEXT" + ")";
    }

    public static class Courses implements BaseColumns{
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_TERM_ID = "termId";
        public static final String COLUMN_MENTOR_ID = "mentorId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_START = "start";
        public static final String COLUMN_EXPECTED_END = "expectedEnd";
        public static final String COLUMN_STATUS = "status";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TERM_ID + " INTEGER, " +
                COLUMN_MENTOR_ID + " INTEGER, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_START + " INTEGER, " +
                COLUMN_EXPECTED_END + " INTEGER, " +
                COLUMN_STATUS + " TEXT" + ")";
    }

    public static class Assessments implements  BaseColumns{
        public static final String TABLE_NAME = "assessments";
        public static final String COLUMN_COURSE_ID = "courseId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DUE = "due";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_ID + " INTEGER, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DUE + " INTEGER" + ")";
    }

    public static class Notes implements BaseColumns{
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_ASSESSMENT_ID = "assessmentId";
        public static final String COLUMN_CONTENT = "content";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ASSESSMENT_ID + " INTEGER, " +
                COLUMN_CONTENT + " TEXT" + ")";
    }
}
