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
    public static final String[] COURSE_COLUMNS = {COURSE_ID, COURSE_TERMID,
            COURSE_TITLE, COURSE_START_DATE, COURSE_END_DATE, COURSE_STATUS, COURSE_MENTOR_NAME,
            COURSE_MENTOR_PHONE, COURSE_MENTOR_EMAIL, COURSE_NOTE};

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

//    public static synchronized DBHelper getInstance(Context context) {
//        if (sQLInstance == null) {
//            sQLInstance = new DBHelper(context.getApplicationContext());
//        }
//        return sQLInstance;
//    }

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
                "FOREIGN KEY (" + COURSE_TERMID + ") REFERENCES " + TERM_TABLE + "(" + COURSE_ID + "))" );
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS assessments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "courseid INTEGER, " +
                "name TEXT, " +
                "type TEXT, " +
                "assessment_date DATE, " +
                "note TEXT, " +
                "FOREIGN KEY (courseid) REFERENCES courses(_id))"
        );
    }

    public void seedData() {
//        this.getWritableDatabase().execSQL("INSERT INTO terms (_id, title, start_date, end_date) VALUES (-1, 'Term 1', '2018-1-01', '2018-06-30')");
//        this.getWritableDatabase().execSQL("INSERT INTO terms (_id, title, start_date, end_date) VALUES (-2, 'Term 2', '2018-07-01', '2018-12-31')");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TERM_ID, -1);
        values.put(TERM_TITLE, "Term 1");
        values.put(TERM_START_DATE, "2018-11-01");
        values.put(TERM_END_DATE, "2018-11-30");
        db.insert(TERM_TABLE, null, values);
        values.clear();
        values.put(TERM_ID, -2);
        values.put(TERM_TITLE, "Term 2");
        values.put(TERM_START_DATE, "2018-11-01");
        values.put(TERM_END_DATE, "2018-11-31");
        db.insert(TERM_TABLE, null, values);

        this.getWritableDatabase().execSQL("INSERT INTO courses " +
                "(_id, termid, title, start_date, end_date, status, mentor_name, mentor_phone, mentor_email, note) " +
                "VALUES (-1, -1, 'Course 101', '2018-01-01', '2018-02-28', 'Complete', 'Joe Smith', 'jsmith@email.com', '123-456-7890', 'Stuff and things')");
        this.getWritableDatabase().execSQL("INSERT INTO courses " +
                "(_id, termid, title, start_date, end_date, status, mentor_name, mentor_phone, mentor_email, note) " +
                "VALUES (-2, -1, 'Course 102', '2018-03-01', '2018-04-30', 'In progress', 'Joe Smith', 'jsmith@email.com', '123-456-7890', 'Stuff and things')");
        this.getWritableDatabase().execSQL("INSERT INTO courses " +
                "(_id, termid, title, start_date, end_date, status, mentor_name, mentor_phone, mentor_email, note) " +
                "VALUES (-3, -2, 'Course 103', '2018-05-01', '2018-07-31', 'Not started', 'Jane Doe', 'jdoe@email.com', '123-456-7890', '')");

        this.getWritableDatabase().execSQL("INSERT INTO assessments " +
                "(id, courseid, name, type, assessment_date, note) " +
                "VALUES (-1, -2, 'Test', 'OA', '2018-04-20', 'More stuff and things')");
        this.getWritableDatabase().execSQL("INSERT INTO assessments " +
                "(id, courseid, name, type, assessment_date, note) " +
                "VALUES (-2, -3, 'Project', 'PA', '2018-07-15', '')");


    }

    public void clearData() {
        this.getWritableDatabase().execSQL("DROP TABLE assessments");
        this.getWritableDatabase().execSQL("DROP TABLE " + COURSE_TABLE);
        this.getWritableDatabase().execSQL("DROP TABLE " + TERM_TABLE);
        createTables();

//        this.getWritableDatabase().execSQL("DELETE FROM assessments");
//        this.getWritableDatabase().execSQL("DELETE FROM courses");
//        this.getWritableDatabase().execSQL("DELETE FROM terms");

//        SQLiteDatabase db = this.getWritableDatabase();
//        String[] whereArgs = {""};
//        db.delete("terms", "", whereArgs);
//        db.delete("courses", "", whereArgs);
//        db.delete("assessments", "", whereArgs);
    }


}
