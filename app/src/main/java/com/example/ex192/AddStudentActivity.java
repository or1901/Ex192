package com.example.ex192;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddStudentActivity extends AppCompatActivity {
    String[] grades = {"6th", "7th", "8th", "9th", "10th", "11th", "12th"};
    Spinner spGrades;
    ArrayAdapter<String> spinnerAdp;
    AlertDialog.Builder adb;
    LinearLayout vaccineDialog;

    DialogInterface.OnClickListener onDialogBtnClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Save button
            if(which == DialogInterface.BUTTON_POSITIVE) {
            }

            // Cancel button
            else if(which == DialogInterface.BUTTON_NEGATIVE) {
                dialog.cancel();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        initViews();
    }

    /**
     * This function initializes the views objects.
     */
    private void initViews() {
        spGrades = findViewById(R.id.spGrades);

        spinnerAdp = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, grades);
        spGrades.setAdapter(spinnerAdp);
    }

    private void displayVaccineDialog(int vaccineNum) {
        vaccineDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.vaccine_dialog, null);

        adb = new AlertDialog.Builder(this);

        adb.setView(vaccineDialog);
        adb.setTitle("Vaccine " + vaccineNum + " Info");
        adb.setCancelable(false);

        adb.setPositiveButton("Save", onDialogBtnClick);
        adb.setNegativeButton("Cancel", onDialogBtnClick);

        adb.show();
    }

    public void getFirstVaccineData(View view) {
        displayVaccineDialog(1);
    }

    public void getSecondVaccineData(View view) {
        displayVaccineDialog(2);
    }
}