package net.lisadean.c196lisadean;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTerm extends AppCompatActivity {
    private DatePicker datePicker;
    private Button okButton;
    private RelativeLayout rl;
    private View selectedButton;
    private String startDate;
    private String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createDatePicker();

    }

    private void createDatePicker() {
        datePicker = findViewById(R.id.term_add_date_picker);
        datePicker.setSpinnersShown(true);
        okButton = findViewById(R.id.term_add_ok_button);
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
        int month = datePicker.getMonth() + 1;
        int day = datePicker.getDayOfMonth();
        date.set(year, month, day);
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        String dateString = df.format(date.getTime());
        if(selectedButton.getId() == R.id.term_start_date_button) {
            TextView textView = findViewById(R.id.term_start_date_display);
            textView.setText(dateString);
            startDate = dateString;
        } else {
            TextView textView = findViewById(R.id.term_end_date_display);
            textView.setText(dateString);
            endDate = dateString;
        }
        datePicker.setVisibility(View.INVISIBLE);
        rl.setVisibility(View.VISIBLE);

    }

    public void setDate(View view) {
        datePicker.setVisibility(View.VISIBLE);
        rl = findViewById(R.id.term_add_main);
        rl.setVisibility(View.INVISIBLE);
        okButton = findViewById(R.id.term_add_ok_button);
        okButton.setVisibility(View.VISIBLE);
        selectedButton = view;
    }

    public void saveTerm(View view) {
        EditText termTitleInput = findViewById(R.id.titleEdit);
        String title = termTitleInput.getText().toString();
        if (!title.trim().isEmpty()) {
            ContentValues cv = new ContentValues();
            cv.put("title", title);
            cv.put("start_date", startDate);
            cv.put("end_date", endDate);
            getContentResolver().insert(TermsProvider.CONTENT_URI, cv);
            Toast.makeText(getApplicationContext(), "Term added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Term could not be added", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View view) {
        Toast.makeText(this, "Term add cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }
}
