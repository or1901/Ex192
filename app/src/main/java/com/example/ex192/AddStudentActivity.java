package com.example.ex192;

import static com.example.ex192.FBRef.REF_STUDENTS;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ex192.Objects.Student;
import com.example.ex192.Objects.Vaccine;

import java.util.Calendar;

public class AddStudentActivity extends AppCompatActivity {
    String[] grades = {"6th", "7th", "8th", "9th", "10th", "11th", "12th"};
    Spinner spGrades;
    ArrayAdapter<String> spinnerAdp;
    AlertDialog.Builder adb;
    LinearLayout vaccineDialog;
    EditText dialogEtPlace, dialogEtDate, etPrivateName, etFamilyName, etId, etClass;
    Switch swCanImmune;
    int currentVaccine;
    Vaccine[] vaccinesData;
    Context activityContext;

    DialogInterface.OnClickListener onDialogBtnClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            // Save button
            if(which == DialogInterface.BUTTON_POSITIVE) {
                if(dialogEtPlace.getText().toString().isEmpty() || dialogEtDate.getText().toString().isEmpty()) {
                    Toast.makeText(activityContext, "Place or date are empty!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    vaccinesData[currentVaccine].setPlaceTaken(dialogEtPlace.getText().toString());
                    Toast.makeText(activityContext, "Vaccine " + (currentVaccine + 1) +
                                    " saved!", Toast.LENGTH_SHORT).show();
                }
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
        etPrivateName = findViewById(R.id.etPrivateName);
        etFamilyName = findViewById(R.id.etFamilyName);
        etId = findViewById(R.id.etId);
        etClass = findViewById(R.id.etClass);
        spGrades = findViewById(R.id.spGrades);
        swCanImmune = findViewById(R.id.swCanImmune);

        spinnerAdp = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, grades);
        spGrades.setAdapter(spinnerAdp);

        currentVaccine = 0;
        vaccinesData = new Vaccine[2];
        vaccinesData[0] = new Vaccine();
        vaccinesData[1] = new Vaccine();

        activityContext = this;
    }

    private void displayVaccineDialog(int vaccineNum) {
        vaccineDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.vaccine_dialog, null);

        dialogEtPlace = vaccineDialog.findViewById(R.id.dialogEtPlace);
        dialogEtDate = vaccineDialog.findViewById(R.id.dialogEtDate);

        adb = new AlertDialog.Builder(this);

        adb.setView(vaccineDialog);
        adb.setTitle("Vaccine " + vaccineNum + " Info");
        adb.setCancelable(false);

        adb.setPositiveButton("Save", onDialogBtnClick);
        adb.setNegativeButton("Cancel", onDialogBtnClick);

        adb.show();
    }

    public void getFirstVaccineData(View view) {
        currentVaccine = 0;
        displayVaccineDialog(1);
    }

    public void getSecondVaccineData(View view) {
        currentVaccine = 1;
        displayVaccineDialog(2);
    }

    public void dialogChooseDate(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddStudentActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dialogEtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        calendar.set(year, monthOfYear + 1, dayOfMonth);

                        vaccinesData[currentVaccine].setDate(calendar);
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    private boolean areFieldsFull() {
        return (!etPrivateName.getText().toString().isEmpty()) &&
                (!etFamilyName.getText().toString().isEmpty()) &&
                (!etId.getText().toString().isEmpty()) &&
                (!etClass.getText().toString().isEmpty());
    }

    private boolean isValidClass() {
        return Integer.parseInt(etClass.getText().toString()) > 0;
    }

    private Student getCurrentStudent() {
        return new Student(etPrivateName.getText().toString(),
                etFamilyName.getText().toString(), etId.getText().toString(),
                spGrades.getSelectedItemPosition() + 6,
                Integer.parseInt(etClass.getText().toString()), swCanImmune.isChecked(),
                vaccinesData[0], vaccinesData[1]
        );
    }

    private void resetEmptyVaccines() {
        if(vaccinesData[0].getPlaceTaken().isEmpty()) {
            vaccinesData[0].setDate(null);
        }
        if(vaccinesData[1].getPlaceTaken().isEmpty()) {
            vaccinesData[1].setDate(null);
        }
    }

    public void saveStudent(View view) {
        if(areFieldsFull()) {
            if(isValidClass()) {
                resetEmptyVaccines();
                Student student = getCurrentStudent();

                REF_STUDENTS.child(etId.getText().toString()).setValue(student);

                Toast.makeText(activityContext, "Student saved!",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(activityContext, "Class number isn't valid!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(activityContext, "There is an empty field!",
                    Toast.LENGTH_SHORT).show();
            }
    }
}