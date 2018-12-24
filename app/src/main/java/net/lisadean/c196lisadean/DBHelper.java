package net.lisadean.c196lisadean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = 'wgu.db';
    private static final int DATABASE_VERSION = 1;
    private static DBHelper sQLInstance;

    private static final String TABLE_TERM = "terms";
    private static final String TABLE_COURSE = "courses";
    private static final String TABLE_ASSESSMENT = "assessments";

    private static final String KEY_TERM_ID = "id";
    private static final String KEY_TERM_TITLE = "title";
    private static final String KEY_TERM_START = "start";
    private static final String KEY_TERM_END = "end";

    private static final String KEY_COURSE_ID = "id";
    private static final String KEY_COURSE_TERM_ID_FK = "termid";
    private static final String KEY_COURSE_TITLE = "title";
    private static final String KEY_COURSE_START = "start";
    private static final String KEY_COURSE_END = "end";
    private static final String KEY_COURSE_STATUS = "status";
    private static final String KEY_COURSE_MENTOR_NAME = "mentorname";
    private static final String KEY_COURSE_MENTOR_PHONE = "mentorphone";
    private static final String KEY_COURSE_MENTOR_EMAIL = "mentoremail";
    private static final String KEY_COURSE_NOTE = "note";

    private static final String KEY_ASSESSMENT_ID = "id";
    private static final String KEY_ASSESSMENT_COURSE_ID_FK = "courseid";
    private static final String KEY_ASSESSMENT_NAME = "name";
    private static final String KEY_ASSESSMENT_TYPE = "type";
    private static final String KEY_ASSESSMENT_DATE = "date";
    private static final String KEY_ASSESSMENT_NOTE = "note";


    private DBHelper(Context context) {
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
}
