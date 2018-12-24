package net.lisadean.c196lisadean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TermList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
    }

    public void add(View view) {
        Intent intent = new Intent(this, AddTerm.class);
        startActivity(intent);
    }
}
