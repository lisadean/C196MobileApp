package net.lisadean.c196lisadean;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CourseList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private CursorAdapter ca;
    private Uri uri;
    private Cursor cursor;
    private String termID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateData();
    }

    private void populateData() {
        Intent intent = getIntent();
        uri = intent.getParcelableExtra(CoursesProvider.CONTENT_ITEM_TYPE);
        termID = uri.getLastPathSegment();

        String filter = DBHelper.TERM_ID + "=" + termID;
        cursor = getContentResolver().query(uri, DBHelper.TERM_COLUMNS, filter, null, null);
        cursor.moveToFirst();
        setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.TERM_TITLE)));

        String[] columns = {DBHelper.TERM_TITLE};
        int[] id = {android.R.id.text1};
        ca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, columns, id,0);
        ListView list = findViewById(R.id.generic_list);
        list.setAdapter(ca);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                uri = Uri.parse(CoursesProvider.CONTENT_URI + "/" + id);
                Intent intent = new Intent(CourseList.this, CourseList.class);
                intent.putExtra(TermsProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, 0);
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    public void add(View view) {
        Intent intent = new Intent(this, TermEditor.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CoursesProvider.CONTENT_URI, DBHelper.COURSE_COLUMNS, DBHelper.COURSE_TERMID + "=" + termID, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ca.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ca.swapCursor(null);
    }
}
