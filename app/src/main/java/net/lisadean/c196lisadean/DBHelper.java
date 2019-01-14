package net.lisadean.c196lisadean;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wgu.db";
    private static final int DATABASE_VERSION = 1;
    private static DBHelper sQLInstance;

    public static final String TERM_TABLE = "terms";
    public static final String TERM_ID = "_id";
    public static final String TERM_TITLE = "title";
    public static final String TERM_START_DATE = "start_date";
    public static final String TERM_END_DATE = "end_date";
    public static final String[] TERM_COLUMNS= {TERM_ID, TERM_TITLE, TERM_START_DATE, TERM_END_DATE};

    public static final String COURSE_TABLE = "courses";
    public static final String COURSE_ID = "_id";
    public static final String COURSE_TERMID = "termid";
    public static final String COURSE_TITLE = "title";
    public static final String COURSE_START_DATE = "start_date";
    public static final String COURSE_END_DATE = "end_date";
    public static final String COURSE_STATUS = "status";
    public static final String COURSE_MENTOR_NAME = "mentor_name";
    public static final String COURSE_MENTOR_PHONE = "mentor_phone";
    public static final String COURSE_MENTOR_EMAIL = "mentor_email";
    public static final String COURSE_NOTE = "note";
    public static final String COURSE_ALARM = "course_alarm";
    public static final String[] COURSE_COLUMNS = {COURSE_ID, COURSE_TERMID,
            COURSE_TITLE, COURSE_START_DATE, COURSE_END_DATE, COURSE_STATUS, COURSE_MENTOR_NAME,
            COURSE_MENTOR_PHONE, COURSE_MENTOR_EMAIL, COURSE_NOTE, COURSE_ALARM};

    public static final String ASSESSMENT_TABLE = "assessments";
    public static final String ASSESSMENT_ID = "_id";
    public static final String ASSESSMENT_COURSEID = "courseid";
    public static final String ASSESSMENT_NAME = "name";
    public static final String ASSESSMENT_TYPE = "type";
    public static final String ASSESSMENT_DATE = "date";
    public static final String ASSESSMENT_NOTE = "note";
    public static final String[] ASSESSMENT_COLUMNS = {ASSESSMENT_ID, ASSESSMENT_COURSEID,
            ASSESSMENT_NAME, ASSESSMENT_TYPE, ASSESSMENT_DATE, ASSESSMENT_NOTE};

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTables() {

        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TERM_TABLE + " (" +
                TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TERM_TITLE + " TEXT, " +
                TERM_START_DATE + " DATE, " +
                TERM_END_DATE + " DATE)");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + COURSE_TABLE+ " (" +
                COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COURSE_TERMID + " INTEGER, " +
                COURSE_TITLE + " TEXT, " +
                COURSE_START_DATE + " DATE, " +
                COURSE_END_DATE + " DATE, " +
                COURSE_STATUS + " TEXT, " +
                COURSE_MENTOR_NAME + " TEXT, " +
                COURSE_MENTOR_PHONE + " TEXT," +
                COURSE_MENTOR_EMAIL + " TEXT," +
                COURSE_NOTE + " TEXT, " +
                COURSE_ALARM + " INTEGER," +
                "FOREIGN KEY (" + COURSE_TERMID + ") REFERENCES " + TERM_TABLE + "(" + COURSE_ID + "))" );
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + ASSESSMENT_TABLE + " (" +
                ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ASSESSMENT_COURSEID + " INTEGER, " +
                ASSESSMENT_NAME + " TEXT, " +
                ASSESSMENT_TYPE + " TEXT, " +
                ASSESSMENT_DATE + " DATE, " +
                ASSESSMENT_NOTE + " TEXT, " +
                "FOREIGN KEY (" + ASSESSMENT_COURSEID + ") REFERENCES " + COURSE_TABLE + "(" + ASSESSMENT_ID + "))"
        );
    }

    public void seedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TERM_ID, -1);
        values.put(TERM_TITLE, "Term 1");
        values.put(TERM_START_DATE, "January 11, 2018");
        values.put(TERM_END_DATE, "2018-11-30");
        db.insert(TERM_TABLE, null, values);
        values.clear();
        values.put(TERM_ID, -2);
        values.put(TERM_TITLE, "Term 2");
        values.put(TERM_START_DATE, "2018-11-01");
        values.put(TERM_END_DATE, "2018-11-31");
        db.insert(TERM_TABLE, null, values);

        this.getWritableDatabase().execSQL("INSERT INTO courses " +
                "(_id, termid, title, start_date, end_date, status, mentor_name, mentor_phone, mentor_email, note, course_alarm) " +
                "VALUES (-1, -1, 'Course 101', '2018-01-01', '2018-02-28', 'Complete', 'Joe Smith', 'jsmith@email.com', '123-456-7890', 'Stuff and things', 0)");
        this.getWritableDatabase().execSQL("INSERT INTO courses " +
                "(_id, termid, title, start_date, end_date, status, mentor_name, mentor_phone, mentor_email, note, course_alarm) " +
                "VALUES (-2, -1, 'Course 102', '2018-03-01', '2018-04-30', 'In progress', 'Joe Smith', 'jsmith@email.com', '123-456-7890', 'Stuff and things', 0)");
        this.getWritableDatabase().execSQL("INSERT INTO courses " +
                "(_id, termid, title, start_date, end_date, status, mentor_name, mentor_phone, mentor_email, note, course_alarm) " +
                "VALUES (-3, -2, 'Course 103', '2018-05-01', '2018-07-31', 'Not started', 'Jane Doe', 'jdoe@email.com', '123-456-7890', '', 0)");

        this.getWritableDatabase().execSQL("INSERT INTO assessments " +
                "(_id, courseid, name, type, date, note) " +
                "VALUES (-1, -2, 'Test', 'OA (Objective Assessment)', '2018-04-20', 'More stuff and things')");
        this.getWritableDatabase().execSQL("INSERT INTO assessments " +
                "(_id, courseid, name, type, date, note) " +
                "VALUES (-2, -3, 'Project', 'PA (Performance Assessment', '2018-07-15', 'More notes')");
        this.getWritableDatabase().execSQL("INSERT INTO assessments " +
                "(_id, courseid, name, type, date, note) " +
                "VALUES (-3, -1, 'Test', 'OA (Objective Assessment)', '2018-07-15', 'Notes and notes')");

    }

    public void clearData() {
        this.getWritableDatabase().execSQL("DROP TABLE " + ASSESSMENT_TABLE);
        this.getWritableDatabase().execSQL("DROP TABLE " + COURSE_TABLE);
        this.getWritableDatabase().execSQL("DROP TABLE " + TERM_TABLE);
        createTables();
    }


}
