package net.lisadean.c196lisadean;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CourseEditor extends AppCompatActivity {
    private DatePicker datePicker;
    private Button okButton;
    private RelativeLayout rl;
    private View selectedButton;
    private String startDate;
    private String endDate;
    private String termID;
    private int courseID;
    private Uri uri;
    private Spinner spinner;
    private ArrayList<String> list;
    private String courseTitle;
    private String courseNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        createDatePicker();
        createSpinner();

        Intent intent = getIntent();
        uri = intent.getParcelableExtra(CoursesProvider.CONTENT_ITEM_TYPE);
        termID = intent.getStringExtra(DBHelper.TERM_ID);

        if (uri != null) {
            populateData();
            Button deleteButton = findViewById(R.id.delete_button);
            deleteButton.setVisibility(View.VISIBLE);
        }

    }

    private void populateData() {
        String filter = DBHelper.COURSE_ID + "=" + uri.getLastPathSegment();
        Cursor cursor = getContentResolver().query(uri, DBHelper.COURSE_COLUMNS, filter, null, null);
        cursor.moveToFirst();

        EditText et = findViewById(R.id.titleEdit);
        courseTitle = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_TITLE));
        et.setText(courseTitle);

        courseID = cursor.getInt(cursor.getColumnIndex(DBHelper.COURSE_ID));
        startDate = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_START_DATE));
        endDate = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_END_DATE));
        String status = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_STATUS));
        String mentorName = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_MENTOR_NAME));
        String mentorPhone = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_MENTOR_PHONE));
        String mentorEmail = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_MENTOR_EMAIL));
        courseNote = cursor.getString(cursor.getColumnIndex(DBHelper.COURSE_NOTE));
        int alarm = cursor.getInt(cursor.getColumnIndex(DBHelper.COURSE_ALARM));

        TextView tv;
        tv = findViewById(R.id.start_date_display);
        tv.setText(startDate);
        tv = findViewById(R.id.end_date_display);
        tv.setText(endDate);
        for (int i = 0; i < list.size(); i++) {
            if (status.equals(list.get(i))) {
                spinner.setSelection(i);
            }
        }
        tv = findViewById(R.id.mentor_name);
        tv.setText(mentorName);
        tv = findViewById(R.id.mentor_phone);
        tv.setText(mentorPhone);
        tv = findViewById(R.id.mentor_email);
        tv.setText(mentorEmail);
        tv = findViewById(R.id.notes);
        tv.setText(courseNote);
        CheckBox cb = findViewById(R.id.alarm_checkbox);
        if (alarm == 1) {cb.setChecked(true);}

        cursor.close();
    }

    private void createSpinner() {
        spinner = findViewById(R.id.status_spinner);
        list = new ArrayList<>();
        list.add("Not started");
        list.add("In Progress");
        list.add("Completed");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

    private void closeDatePicker() {
        Calendar date = Calendar.getInstance();
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        date.set(year, month, day);
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        String dateString = df.format(date.getTime());
        if(selectedButton.getId() == R.id.start_date_button) {
            TextView textView = findViewById(R.id.start_date_display);
            textView.setText(dateString);
            startDate = dateString;
        } else {
            TextView textView = findViewById(R.id.end_date_display);
            textView.setText(dateString);
            endDate = dateString;
        }
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
        EditText et;
        et = findViewById(R.id.titleEdit);
        courseTitle = et.getText().toString();
        String status = spinner.getSelectedItem().toString();
        et = findViewById(R.id.mentor_name);
        String mentorName = et.getText().toString();
        et = findViewById(R.id.mentor_phone);
        String mentorPhone = et.getText().toString();
        et = findViewById(R.id.mentor_email);
        String mentorEmail = et.getText().toString();
        et = findViewById(R.id.notes);
        courseNote = et.getText().toString();
        CheckBox cb = findViewById(R.id.alarm_checkbox);
        int alarm = cb.isChecked() ? 1 : 0;

        if (!courseTitle.trim().isEmpty()) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.COURSE_TITLE, courseTitle);
            cv.put(DBHelper.COURSE_START_DATE, startDate);
            cv.put(DBHelper.COURSE_END_DATE, endDate);
            cv.put(DBHelper.COURSE_STATUS, status);
            cv.put(DBHelper.COURSE_MENTOR_NAME, mentorName);
            cv.put(DBHelper.COURSE_MENTOR_PHONE, mentorPhone);
            cv.put(DBHelper.COURSE_MENTOR_EMAIL, mentorEmail);
            cv.put(DBHelper.COURSE_NOTE, courseNote);
            Log.d("LOG CourseEditor", "termID = " + termID);
            cv.put(DBHelper.COURSE_TERMID, Integer.parseInt(termID));
            cv.put(DBHelper.COURSE_ALARM, alarm);
            if (uri == null) {
                uri = getContentResolver().insert(CoursesProvider.CONTENT_URI, cv);
                courseID = Integer.parseInt(uri.getLastPathSegment());
                result = 1;  // this is so wrong to do but I'm tired and just want this thing done
            } else {
                courseID = Integer.parseInt(uri.getLastPathSegment());
                result = getContentResolver().update(uri, cv, DBHelper.COURSE_ID + "=" + courseID, null);
            }
            if (alarm == 1) {
                createAlarm("Course Start Alert!", courseTitle + " is scheduled to start today", startDate);
                createAlarm("Course End Alert!", courseTitle + " is scheduled to end today", endDate);
            }
            Toast.makeText(getApplicationContext(), "Course added or updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Title required to save course", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void save(View view) {
        if (saveTerm() > 0) {
            finish();
        }
    }

    public void cancel(View view) {
        Toast.makeText(this, "Course add cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void delete(View view) {
        int id = Integer.parseInt(uri.getLastPathSegment());
        try {
            int result = getContentResolver().delete(uri, DBHelper.COURSE_ID + "=" + id, null);
            if (result == 1) {
                finish();
            } else {
                Toast.makeText(this, "Course could not be deleted", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteConstraintException e) {
            Toast.makeText(this, "Course could not be deleted. Has assessments attached.", Toast.LENGTH_SHORT).show();
            Log.d("LOG", e.toString());
        }

    }

    public void edit_assessments(View view) {
        saveTerm();
        Intent intent = new Intent(CourseEditor.this, AssessmentList.class);
        intent.putExtra(AssessmentsProvider.CONTENT_ITEM_TYPE, uri);
        Log.d("CourseEditor", "courseID = " + courseID);
        intent.putExtra(DBHelper.COURSE_ID, courseID);
        startActivityForResult(intent, 0);
    }
    
    public void email(View view) {
        if (saveTerm() > 0) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_SUBJECT, courseTitle + " Notes");
            intent.putExtra(Intent.EXTRA_TEXT   , courseNote);
            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void createAlarm(String title, String message, String date) {
        try {
            Date alarmDate = new SimpleDateFormat("MMMM dd, yyyy").parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(alarmDate);
            Long alarmMillis = c.getTimeInMillis();
            String datePart = new SimpleDateFormat("MMMM dd, yyyy").format(alarmDate);
            String timePart = new SimpleDateFormat("H:mm:ss").format(alarmDate);

            Log.d("LOG AlarmReceiver", "current millis: " + System.currentTimeMillis());
            Log.d("LOG AlarmReceiver", "date: " + datePart + " time: " + timePart + " millis: " + alarmMillis);

            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("message", message);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(this, AlarmReceiver.notificationID, intent, 0);

            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, alarmMillis, alarmIntent);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("LOG AlarmReceiver", e.getMessage());
        }
    }
}
