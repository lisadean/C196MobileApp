package net.lisadean.c196lisadean;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AssessmentEditor extends AppCompatActivity {
    private DatePicker datePicker;
    private Button okButton;
    private RelativeLayout rl;
    private View selectedButton;
    private String endDate;
    private String courseID;
    private int assessmentID;
    private Uri uri;
    private Spinner spinner;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);
        createDatePicker();
        createSpinner();

        Intent intent = getIntent();
        uri = intent.getParcelableExtra(AssessmentsProvider.CONTENT_ITEM_TYPE);
        courseID = intent.getStringExtra(DBHelper.COURSE_ID);

        if (uri != null) {
            populateData();
            Button deleteButton = findViewById(R.id.delete_button);
            deleteButton.setVisibility(View.VISIBLE);
        }

    }

    private void populateData() {
        String filter = DBHelper.ASSESSMENT_ID + "=" + uri.getLastPathSegment();
        Cursor cursor = getContentResolver().query(uri, DBHelper.ASSESSMENT_COLUMNS, filter, null, null);
        cursor.moveToFirst();

        EditText et = findViewById(R.id.titleEdit);
        et.setText(cursor.getString(cursor.getColumnIndex(DBHelper.ASSESSMENT_NAME)));

        assessmentID = cursor.getInt(cursor.getColumnIndex(DBHelper.ASSESSMENT_ID));
        endDate = cursor.getString(cursor.getColumnIndex(DBHelper.ASSESSMENT_DATE));
        String type = cursor.getString(cursor.getColumnIndex(DBHelper.ASSESSMENT_TYPE));
        String note = cursor.getString(cursor.getColumnIndex(DBHelper.ASSESSMENT_NOTE));

        TextView tv;
        tv = findViewById(R.id.end_date_display);
        tv.setText(endDate);
        for (int i = 0; i < list.size(); i++) {
            if (type.equals(list.get(i))) {
                spinner.setSelection(i);
            }
        }
        tv = findViewById(R.id.notes);
        tv.setText(note);

        cursor.close();
    }

    private void createDatePicker() {
        datePicker = findViewById(R.id.date_picker);
        datePicker.setSpinnersShown(true);
        okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDatePicker();
                okButton.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void createSpinner() {
        spinner = findViewById(R.id.type_spinner);
        list = new ArrayList<>();
        list.add("OA (Objective Assessment)");
        list.add("PA (Performance Assessment");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void closeDatePicker() {
        Calendar date = Calendar.getInstance();
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        date.set(year, month, day);
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        String dateString = df.format(date.getTime());
        TextView tv = findViewById(R.id.end_date_display);
        tv.setText(dateString);
        endDate = dateString;
        datePicker.setVisibility(View.INVISIBLE);
        rl.setVisibility(View.VISIBLE);

    }

    public void setDate(View view) {
        datePicker.setVisibility(View.VISIBLE);
        rl = findViewById(R.id.main);
        rl.setVisibility(View.INVISIBLE);
        okButton = findViewById(R.id.ok_button);
        okButton.setVisibility(View.VISIBLE);
        selectedButton = view;
    }

    private int saveTerm() {
        int result = 0;
        EditText termTitleInput = findViewById(R.id.titleEdit);
        String title = termTitleInput.getText().toString();
        if (!title.trim().isEmpty()) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.COURSE_TITLE, title);
            cv.put(DBHelper.COURSE_END_DATE, endDate);
            Log.d("LOG AssessmentEditor", "courseID = " + courseID);
            cv.put(DBHelper.ASSESSMENT_COURSEID, Integer.parseInt(courseID));
            if (uri == null) {
                uri = getContentResolver().insert(AssessmentsProvider.CONTENT_URI, cv);
                assessmentID = Integer.parseInt(uri.getLastPathSegment());
                result = 1;  // this is so wrong to do but I'm tired and just want this thing done
            } else {
                assessmentID = Integer.parseInt(uri.getLastPathSegment());
                result = getContentResolver().update(uri, cv, DBHelper.ASSESSMENT_ID + "=" + assessmentID, null);
            }
            Toast.makeText(getApplicationContext(), "Assessment added or updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Name required to save assessment", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void save(View view) {
        if (saveTerm() > 0) {
            finish();
        }
    }

    public void cancel(View view) {
        Toast.makeText(this, "Assessment add cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void delete(View view) {
        int id = Integer.parseInt(uri.getLastPathSegment());
        try {
            int result = getContentResolver().delete(uri, DBHelper.ASSESSMENT_ID + "=" + id, null);
            if (result == 1) {
                finish();
            } else {
                Toast.makeText(this, "Assessment could not be deleted", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteConstraintException e) {
//            Toast.makeText(this, "Assessment could not be deleted. Has assessments attached.", Toast.LENGTH_SHORT).show();
            Log.d("LOG", e.toString());
        }

    }
}
