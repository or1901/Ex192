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

/**
 * Add Student Activity:
 * inputs the data of new Students and saves them in the DB.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 9/4/2024
 */
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

        /**
         * This function reacts to the choice of the user in the vaccine alert dialog.
         * @param dialog The vaccine alert dialog.
         * @param which The alert dialog button clicked.
         */
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

    /**
     * This function displays an alert dialog to input the details of a vaccine.
     * @param vaccineNum The number of the vaccine to get its details.
     */
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

    /**
     * This function displays the first vaccine alert dialog when the suitable button is clicked.
     * @param view The view object of the button that was clicked.
     */
    public void getFirstVaccineData(View view) {
        if(swCanImmune.isChecked()) {
            currentVaccine = 0;
            displayVaccineDialog(1);
        }
        else {
            Toast.makeText(activityContext, "Student can't immune!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This function displays the second vaccine alert dialog when the suitable button is clicked.
     * @param view The view object of the button that was clicked.
     */
    public void getSecondVaccineData(View view) {
        if(swCanImmune.isChecked()) {
            currentVaccine = 1;
            displayVaccineDialog(2);
        }
        else {
            Toast.makeText(activityContext, "Student can't immune!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This function opens a date picker dialog when the user clicks on the select date edit text
     * in the vaccine alert dialog. It saves the user's choice in the current vaccine instance.
     * @param view The view object of the select date edit text.
     */
    public void dialogChooseDate(View view) {
        // Saves the current time details, in order to display it in the picker
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddStudentActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    /**
                     * This function saves the user's choice in the date picker.
                     * @param view The view object of the date picker.
                     * @param year The selected year.
                     * @param monthOfYear The selected month.
                     * @param dayOfMonth The selected day.
                     */
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

    /**
     * This function checks if all the edit text fields contain content in them.
     * @return Whether all the edit text fields contain content, or not.
     */
    private boolean areFieldsFull() {
        return (!etPrivateName.getText().toString().isEmpty()) &&
                (!etFamilyName.getText().toString().isEmpty()) &&
                (!etId.getText().toString().isEmpty()) &&
                (!etClass.getText().toString().isEmpty());
    }

    /**
     * This function checks if the class the user entered is valid(bigger than 0).
     * @return Whether the class the user entered is valid, or not.
     */
    private boolean isValidClass() {
        return Integer.parseInt(etClass.getText().toString()) > 0;
    }

    /**
     * This function gets a Student instance initialized with the current data saved in the
     * activity views.
     * @return A Student instance initialized with the current data saved in the activity views.
     */
    private Student getCurrentStudent() {
        return new Student(etPrivateName.getText().toString(),
                etFamilyName.getText().toString(), etId.getText().toString(),
                spGrades.getSelectedItemPosition() + 6,
                Integer.parseInt(etClass.getText().toString()), swCanImmune.isChecked(),
                vaccinesData[0], vaccinesData[1]
        );
    }

    /**
     * This function resets the date field of the saved vaccines if there isn't a place saved in
     * them - may occur when the user chose only date and then closed the alert dialog.
     */
    private void resetEmptyVaccines() {
        if(vaccinesData[0].getPlaceTaken().isEmpty()) {
            vaccinesData[0].setDate(null);
        }
        if(vaccinesData[1].getPlaceTaken().isEmpty()) {
            vaccinesData[1].setDate(null);
        }
    }

    /**
     * This function saves the current Student's data in the DB - if all the fields are valid.
     * @param view The view object of the button that was clicked in order to save the Student.
     */
    public void saveStudent(View view) {
        if(areFieldsFull())
        {
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