package net.lisadean.c196lisadean;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AssessmentsProvider extends ContentProvider{
    private static final String AUTHORITY = "net.lisadean.c196lisadean.assessmentsprovider";
    private static final String BASE_PATH = "assessments";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    public static final String CONTENT_ITEM_TYPE = "Assessment";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ASSESSMENTS = 1;
    private static final int ASSESSMENTS_ID = 2;

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, ASSESSMENTS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ASSESSMENTS_ID);
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

        if (uriMatcher.match(uri) == ASSESSMENTS_ID) {
            selection = DBHelper.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
        }
        return database.query(DBHelper.ASSESSMENT_TABLE, DBHelper.ASSESSMENT_COLUMNS, selection,
                null, null, null, DBHelper.ASSESSMENT_DATE + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBHelper.ASSESSMENT_TABLE, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(DBHelper.ASSESSMENT_TABLE, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBHelper.ASSESSMENT_TABLE, values, selection, selectionArgs);
    }
}
