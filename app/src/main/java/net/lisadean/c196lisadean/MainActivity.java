package net.lisadean.c196lisadean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DBHelper dBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dBHelper = new DBHelper(MainActivity.this);
        dBHelper.getWritableDatabase();
        Toast.makeText(MainActivity.this, "Database: " + dBHelper.getDatabaseName(), Toast.LENGTH_SHORT).show();
        dBHelper.createTables();
//        dBHelper.seedData();
    }

    public void openTerms(View view) {
        Intent intent = new Intent(this, TermList.class);
        startActivity(intent);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        dBHelper.close();
//        Toast.makeText(MainActivity.this, "Database closed: " + dBHelper.getDatabaseName(), Toast.LENGTH_SHORT).show();
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_seed_data:
                dBHelper.seedData();
                break;
            case R.id.action_clear_data:
                dBHelper.clearData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
