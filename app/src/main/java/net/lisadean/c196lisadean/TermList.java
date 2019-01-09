package net.lisadean.c196lisadean;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TermList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private CursorAdapter ca;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateData();
    }

    private void populateData() {
        String[] columns = {"title"};
        int[] id = {android.R.id.text1};
        ca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, columns, id,0);
        ListView list = findViewById(R.id.generic_list);
        list.setAdapter(ca);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                uri = Uri.parse(TermsProvider.CONTENT_URI + "/" + id);
                Intent intent = new Intent(TermList.this, TermList.class);
                intent.putExtra(TermsProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, 0);
            }
        });
    }

    public void add(View view) {
        Intent intent = new Intent(this, AddTerm.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, TermsProvider.CONTENT_URI, null, null, null, null);
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
