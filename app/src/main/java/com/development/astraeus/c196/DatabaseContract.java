package com.development.astraeus.c196;

import android.provider.BaseColumns;

/**
 * Created by Astraeus on 10/15/2017.
 */

class DatabaseContract {
    //Store used queries as final strings here
    static final String SELECT_ALL_TERMS = "SELECT * FROM " + Terms.TABLE_NAME + " ORDER BY " + Terms.COLUMN_START;
    static final String SELECT_ALL_TERM_TITLES_ORDERED_BY_START = "SELECT " + Terms.COLUMN_TITLE + " FROM " + Terms.TABLE_NAME + " ORDER BY " + Terms.COLUMN_START;
    static final String SELECT_TERM_BY_ID = "SELECT * FROM " + Terms.TABLE_NAME + " WHERE " + Terms._ID + "=?";
    static final String SELECT_TERM_BY_TITLE = "SELECT * FROM " + Terms.TABLE_NAME + " WHERE " + Terms.COLUMN_TITLE + " LIKE ?";
    static final String SELECT_ALL_COURSES_BY_TERM = "SELECT * FROM " + Courses.TABLE_NAME + " WHERE " + Courses.COLUMN_TERM_ID + " =?";
    static final String SELECT_ALL_COURSES_ORDERED_BY_TERM = "SELECT * FROM " + Courses.TABLE_NAME + " ORDER BY " + Courses.COLUMN_TERM_ID;
    static final String SELECT_ALL_COURSE_TITLES_ORDERED_BY_START = "SELECT * FROM " + Courses.TABLE_NAME + " ORDER BY " + Courses.COLUMN_START;
    static final String SELECT_COURSE_BY_ID = "SELECT * FROM " + Courses.TABLE_NAME + " WHERE " + Courses._ID + "=?";
    static final String SELECT_COURSE_BY_TITLE = "SELECT * FROM " + Courses.TABLE_NAME + " WHERE " + Courses.COLUMN_TITLE + " LIKE ?";
    static final String SELECT_ALL_MENTOR_NAMES_ORDERED_ALPHABETICALLY = "SELECT " + Courses.COLUMN_MENTOR_NAME + " FROM " + Courses.TABLE_NAME + " ORDER BY " + Courses.COLUMN_MENTOR_NAME;
    static final String SELECT_ALL_ASSESSMENTS_ORDERED_BY_COURSE = "SELECT * FROM " + Assessments.TABLE_NAME + " ORDER BY " + Assessments.COLUMN_COURSE_ID;
    static final String SELECT_ALL_ASSESSMENTS_FOR_COURSE = "SELECT * FROM " + Assessments.TABLE_NAME + " WHERE " + Assessments.COLUMN_COURSE_ID + "=?";
    static final String SELECT_ALL_NOTES_FOR_COURSE = "SELECT * FROM " + Notes.TABLE_NAME + " WHERE " + Notes.COLUMN_COURSE_ID + "=?";

    private DatabaseContract(){}

    static class Terms implements BaseColumns{
        static final String TABLE_NAME = "terms";
        static final String COLUMN_TITLE = "termTitle";
        static final String COLUMN_START = "termStart";
        static final String COLUMN_END = "termEnd";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_START + " INTEGER, " +
                COLUMN_END + " INTEGER" + ")";
    }

    static class Courses implements BaseColumns{
        static final String TABLE_NAME = "courses";
        static final String COLUMN_TERM_ID = "termId";
        static final String COLUMN_MENTOR_NAME = "mentorName";
        static final String COLUMN_MENTOR_PHONE_NUMBER = "mentorPhoneNumber";
        static final String COLUMN_MENTOR_EMAIL = "mentorEmail";
        static final String COLUMN_TITLE = "courseTitle";
        static final String COLUMN_START = "courseStart";
        static final String COLUMN_EXPECTED_END = "courseExpectedEnd";
        static final String COLUMN_STATUS = "courseStatus";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TERM_ID + " INTEGER, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_START + " INTEGER, " +
                COLUMN_EXPECTED_END + " INTEGER, " +
                COLUMN_MENTOR_NAME + " TEXT, " +
                COLUMN_MENTOR_PHONE_NUMBER + " TEXT, " +
                COLUMN_MENTOR_EMAIL + " TEXT, " +
                COLUMN_STATUS + " TEXT" + ")";
    }

    static class Assessments implements  BaseColumns{
        static final String TABLE_NAME = "assessments";
        static final String COLUMN_COURSE_ID = "courseId";
        static final String COLUMN_TITLE = "assessmentTitle";
        static final String COLUMN_TYPE = "assessmentType";
        static final String COLUMN_DUE = "assessmentDue";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_ID + " INTEGER, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_DUE + " INTEGER" + ")";
    }

    static class Notes implements BaseColumns{
        static final String TABLE_NAME = "notes";
        static final String COLUMN_COURSE_ID = "courseId";
        static final String COLUMN_CONTENT = "noteContent";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_ID + " INTEGER, " +
                COLUMN_CONTENT + " TEXT" + ")";
    }
}
