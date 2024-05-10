package com.example.ex192;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SortAndFilterActivity extends AppCompatActivity {
    String[] filterOptions = {"All students of a given class", "All students of a given grade",
            "All immune students(in grades order)", "All unimmune students"};
    Spinner spFilterOptions;
    ArrayAdapter<String> spinnerAdp;
    Intent gi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_and_filter);

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        gi = getIntent();
    }

    private void initViews() {
        spFilterOptions = findViewById(R.id.spFilterOptions);

        spinnerAdp = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, filterOptions);
        spFilterOptions.setAdapter(spinnerAdp);
    }

    /**
     * This function presents the options menu for moving between activities.
     * @param menu the options menu in which you place your items.
     * @return true in order to show the menu, otherwise false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This function reacts to the user choice in the options menu - it moves to the chosen
     * activity from the menu, or resets the current one.
     * @param item the menu item that was selected.
     * @return must return true for the menu to react.
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if(id == R.id.menuAddStudent) {
            gi = new Intent(this,AddStudentActivity.class);
            startActivity(gi);
        }
        else if(id == R.id.menuAllStudents) {
            gi.setClass(this, AllStudentsActivity.class);
            startActivity(gi);
        }

        return super.onOptionsItemSelected(item);
    }
}