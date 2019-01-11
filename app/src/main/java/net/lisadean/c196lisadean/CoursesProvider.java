package net.lisadean.c196lisadean;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class CoursesProvider extends ContentProvider{
    private static final String AUTHORITY = "net.lisadean.c196lisadean.coursesprovider";
    private static final String BASE_PATH = "courses";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    public static final String CONTENT_ITEM_TYPE = "Course";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int COURSES = 1;
    private static final int COURSES_ID = 2;

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, COURSES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", COURSES_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBHelper helper = new DBHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if (uriMatcher.match(uri) == COURSES_ID) {
            selection = DBHelper.COURSE_ID + "=" + uri.getLastPathSegment();
        }
//        Log.d("LOG", "Selection: " + selection);
        return database.query(DBHelper.COURSE_TABLE, DBHelper.COURSE_COLUMNS, selection, null, null, null, DBHelper.COURSE_START_DATE + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBHelper.COURSE_TABLE, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBHelper.COURSE_TABLE, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBHelper.COURSE_TABLE, values, selection, selectionArgs);
    }
}
