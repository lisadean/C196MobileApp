package net.lisadean.c196lisadean;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AssessmentList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private CursorAdapter ca;
    private Uri uri;
    private String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateData();
    }

    private void populateData() {
        Intent intent = getIntent();
        uri = intent.getParcelableExtra(AssessmentsProvider.CONTENT_ITEM_TYPE);
        courseID = uri.getLastPathSegment();

        String filter = DBHelper.COURSE_ID + "=" + courseID;
        Cursor cursor = getContentResolver().query(uri, DBHelper.COURSE_COLUMNS, filter, null, null);
        cursor.moveToFirst();
        setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_TITLE)));
        cursor.close();

        String[] columns = {DBHelper.ASSESSMENT_NAME};
        int[] id = {android.R.id.text1};
        ca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, columns, id,0);
        ListView list = findViewById(R.id.generic_list);
        list.setAdapter(ca);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                uri = Uri.parse(AssessmentsProvider.CONTENT_URI + "/" + id);
                Intent intent = new Intent(AssessmentList.this, AssessmentEditor.class);
                intent.putExtra(AssessmentsProvider.CONTENT_ITEM_TYPE, uri);
                intent.putExtra(DBHelper.COURSE_ID, courseID);
                startActivityForResult(intent, 0);
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    public void add(View view) {
        // Change to AssessmentEditor
        Intent intent = new Intent(this, AssessmentEditor.class);
        intent.putExtra(DBHelper.COURSE_ID, courseID);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AssessmentsProvider.CONTENT_URI, DBHelper.ASSESSMENT_COLUMNS,
                DBHelper.ASSESSMENT_COURSEID + "=" + courseID, null, null);
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
