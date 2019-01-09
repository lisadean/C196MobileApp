package net.lisadean.c196lisadean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wgu.db";
    private static final int DATABASE_VERSION = 1;
    private static DBHelper sQLInstance;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static synchronized DBHelper getInstance(Context context) {
        if (sQLInstance == null) {
            sQLInstance = new DBHelper(context.getApplicationContext());
        }
        return sQLInstance;
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

        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS terms (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "start_date DATE, " +
                "end_date DATE)");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS courses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "termid INTEGER, " +
                "title TEXT, " +
                "start_date DATE, " +
                "end_date DATE, " +
                "status TEXT, " +
                "mentor_name TEXT, " +
                "mentor_phone TEXT," +
                "mentor_email TEXT," +
                "note TEXT, " +
                "FOREIGN KEY (termid) REFERENCES terms(_id))" );
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS assessments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "courseid INTEGER, " +
                "name TEXT, " +
                "type TEXT, " +
                "assessment_date DATE, " +
                "note TEXT, " +
                "FOREIGN KEY (courseid) REFERENCES courses(id))"
        );
    }

    public void seedData() {
        this.getWritableDatabase().execSQL("INSERT INTO terms (_id, title, start_date, end_date) VALUES (-1, 'Term 1', '2018-1-01', '2018-06-30')");
        this.getWritableDatabase().execSQL("INSERT INTO terms (_id, title, start_date, end_date) VALUES (-2, 'Term 2', '2018-07-01', '2018-12-31')");

        this.getWritableDatabase().execSQL("INSERT INTO courses " +
                "(id, termid, title, start_date, end_date, status, mentor_name, mentor_phone, mentor_email, note) " +
                "VALUES (-1, -1, 'Course 101', '2018-01-01', '2018-02-28', 'Complete', 'Joe Smith', 'jsmith@email.com', '123-456-7890', 'Stuff and things')");
        this.getWritableDatabase().execSQL("INSERT INTO courses " +
                "(id, termid, title, start_date, end_date, status, mentor_name, mentor_phone, mentor_email, note) " +
                "VALUES (-2, -1, 'Course 102', '2018-03-01', '2018-04-30', 'In progress', 'Joe Smith', 'jsmith@email.com', '123-456-7890', 'Stuff and things')");
        this.getWritableDatabase().execSQL("INSERT INTO courses " +
                "(id, termid, title, start_date, end_date, status, mentor_name, mentor_phone, mentor_email, note) " +
                "VALUES (-3, -2, 'Course 103', '2018-05-01', '2018-07-31', 'Not started', 'Jane Doe', 'jdoe@email.com', '123-456-7890', '')");

        this.getWritableDatabase().execSQL("INSERT INTO assessments " +
                "(id, courseid, name, type, assessment_date, note) " +
                "VALUES (-1, -2, 'Test', 'OA', '2018-04-20', 'More stuff and things')");
        this.getWritableDatabase().execSQL("INSERT INTO assessments " +
                "(id, courseid, name, type, assessment_date, note) " +
                "VALUES (-2, -3, 'Project', 'PA', '2018-07-15', '')");

//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("title", "Term 3");
//        values.put("start_date", "2018-11-01");
//        values.put("end_date", "2018-11-30");
//        db.insert("terms", null, values);
//        values.clear();
//        values.put("title", "Term 4");
//        values.put("start_date", "2018-11-01");
//        values.put("end_date", "2018-11-31");
//        db.insert("terms", null, values);
    }

    public void clearData() {
        this.getWritableDatabase().execSQL("DROP TABLE assessments");
        this.getWritableDatabase().execSQL("DROP TABLE courses");
        this.getWritableDatabase().execSQL("DROP TABLE terms");
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
