package net.lisadean.c196lisadean;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TermsProvider extends ContentProvider {

    private static final String AUTHORITY = "net.lisadean.c196lisadean.termsprovider";
    private static final String BASE_PATH = "terms";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    public static final String CONTENT_ITEM_TYPE = "Term";


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int TERMS = 1;
    private static final int TERMS_ID = 2;

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, TERMS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TERMS_ID);
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

        if (uriMatcher.match(uri) == TERMS_ID) {
            selection = "_id=" + uri.getLastPathSegment();
        }
        String[] columns = {"_id", "title", "start_date", "end_date"};
        return database.query("terms",columns, selection, null, null, null, "start_date DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert("terms", null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete("terms", selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update("terms", values, selection, selectionArgs);
    }
}
