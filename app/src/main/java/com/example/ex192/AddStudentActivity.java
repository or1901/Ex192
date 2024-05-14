package com.example.ex192;

import static com.example.ex192.FBRef.REF_STUDENTS;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ex192.Objects.Student;
import com.example.ex192.Objects.Vaccine;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * Add Student Activity:
 * inputs the data of new Students and saves them in the DB.
 * @author Ori Roitzaid <or1901 @ bs.amalnet.k12.il>
 * @version	1
 * @since 9/4/2024
 */
public class AddStudentActivity extends AppCompatActivity {
    String[] grades = {"7th", "8th", "9th", "10th", "11th", "12th"};
    Spinner spGrades;
    ArrayAdapter<String> spinnerAdp;
    AlertDialog.Builder adb;
    AlertDialog ad;
    LinearLayout vaccineDialog;
    EditText dialogEtPlace, dialogEtDate, etPrivateName, etFamilyName, etId, etClass;
    Switch swCanImmune;
    TextView tvAddStudent;
    int currentVaccine;
    Vaccine[] vaccinesData;
    Context activityContext;
    Intent si;
    ArrayList<String> idsList;
    Student savedStudent;
    boolean editMode;

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

        initAll();
    }

    @Override
    protected void onStart(){
        super.onStart();

        savedStudent = null;
        si = getIntent();
        savedStudent = (Student) si.getParcelableExtra("Student");
        si.removeExtra("Student");
        idsList.clear();

        if(savedStudent != null)
        {
            editMode = true;
            tvAddStudent.setText("Edit Student");
            displayStudentFields(savedStudent);
        }
        else
        {
            tvAddStudent.setText("Add Student");
            getSavedIds();
        }
    }

    /**
     * This function initializes the views objects and all other objects.
     */
    private void initAll() {
        etPrivateName = findViewById(R.id.etPrivateName);
        etFamilyName = findViewById(R.id.etFamilyName);
        etId = findViewById(R.id.etId);
        etClass = findViewById(R.id.etClass);
        spGrades = findViewById(R.id.spGrades);
        swCanImmune = findViewById(R.id.swCanImmune);
        tvAddStudent = findViewById(R.id.tvAddStudent);

        spinnerAdp = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, grades);
        spGrades.setAdapter(spinnerAdp);

        currentVaccine = 0;
        vaccinesData = new Vaccine[2];
        vaccinesData[0] = new Vaccine();
        vaccinesData[1] = new Vaccine();

        activityContext = this;
        si = new Intent();
        idsList = new ArrayList<>();
        editMode = false;
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

        ad = adb.create();
        ad.show();

        displaySavedVaccineData(vaccinesData[currentVaccine]);
    }

    /**
     * This function displays the info of a given vaccine in the alert dialog.
     * @param vaccine The given vaccine to display its data.
     */
    private void displaySavedVaccineData(Vaccine vaccine) {
        if((vaccine != null) && (!vaccine.getPlaceTaken().isEmpty()))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(vaccine.getDate());

            dialogEtPlace.setText(vaccine.getPlaceTaken());
            dialogEtDate.setText(dateFormat.format(calendar.getTime()));
        }
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
            if(vaccinesData[0] != null && !vaccinesData[0].getPlaceTaken().isEmpty()) {
                currentVaccine = 1;
                displayVaccineDialog(2);
            }
            else {
                Toast.makeText(activityContext, "You must enter the first vaccine before the second!",
                        Toast.LENGTH_LONG).show();
            }
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
        // Saves dates for future use
        Calendar nowCalendar = Calendar.getInstance();
        Calendar savedCalendar = Calendar.getInstance();
        Calendar chosenDate = Calendar.getInstance();

        // Checks if a date is already saved
        if((vaccinesData[currentVaccine] != null) && (vaccinesData[currentVaccine].getDate() != 0))
        {
            savedCalendar.setTimeInMillis(vaccinesData[currentVaccine].getDate());
        }
        else if(vaccinesData[currentVaccine] == null)  // Checks if there is no vaccine object
        {
            vaccinesData[currentVaccine] = new Vaccine();
        }

        int year = savedCalendar.get(Calendar.YEAR);
        int month = savedCalendar.get(Calendar.MONTH);
        int day = savedCalendar.get(Calendar.DAY_OF_MONTH);

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
                        chosenDate.set(year, monthOfYear, dayOfMonth);

                        if(chosenDate.after(nowCalendar)) {
                            Toast.makeText(activityContext, "You can't choose a future date!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dialogEtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            vaccinesData[currentVaccine].setDate(chosenDate.getTimeInMillis());
                        }
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
                getSelectedGrade(),
                Integer.parseInt(etClass.getText().toString()), swCanImmune.isChecked(),
                vaccinesData[0], vaccinesData[1]
        );
    }

    /**
     * This function resets the date field of the saved vaccines if there isn't a place saved in
     * them - may occur when the user chose only date and then closed the alert dialog.
     */
    private void resetEmptyVaccines() {
        if((vaccinesData[0] != null) && (vaccinesData[0].getPlaceTaken().isEmpty())) {
            vaccinesData[0] = null;
        }
        if((vaccinesData[1] != null) && (vaccinesData[1].getPlaceTaken().isEmpty()))  {
            vaccinesData[1] = null;
        }
    }

    /**
     * This function gets the selected grade in the grades spinner.
     * @return The selected grade in the grades spinner.
     */
    private int getSelectedGrade() {
        return spGrades.getSelectedItemPosition() + 7;
    }

    /**
     * This function gets all the students ids from the DB, and saves it in the ids array list.
     */
    private void getSavedIds() {
        REF_STUDENTS.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idsList.clear();

                for(DataSnapshot canImmunedData : snapshot.getChildren()) {
                    for (DataSnapshot gradeData : canImmunedData.getChildren()) {
                        for (DataSnapshot classData : gradeData.getChildren()) {
                            for (DataSnapshot studData : classData.getChildren()) {
                                idsList.add(studData.getKey());
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activityContext, "Failed reading from the DB!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This function saves the current Student's data in the DB - if all the fields are valid.
     * @param view The view object of the button that was clicked in order to save the Student.
     */
    public void saveStudent(View view) {
        if(areFieldsFull())
        {
            if(isValidClass())
            {
                if(!idsList.contains(etId.getText().toString()))  // If id doesn't exist
                {
                    resetEmptyVaccines();
                    validateVaccinesSaving(swCanImmune.isChecked());
                    Student student = getCurrentStudent();

                    // If in edit mode, deletes the old object if needed
                    if(editMode) {
                        deleteOldWhenNeeded(savedStudent, student);
                    }

                    saveStudInDB(student);

                    Toast.makeText(activityContext, "Student saved!",
                            Toast.LENGTH_SHORT).show();

                    if(!editMode) {
                        vaccinesData[0] = new Vaccine();
                        vaccinesData[1] = new Vaccine();
                        idsList.add(etId.getText().toString());  // Adds the new student id to the list
                    }
                    else {
                        savedStudent = new Student(student);
                    }
                }

                else {
                    Toast.makeText(activityContext, "Student id already exists!",
                            Toast.LENGTH_SHORT).show();
                }
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

    /**
     * This function displays a given student data in the activity fields.
     * @param student The student object to display its fields.
     */
    private void displayStudentFields(Student student) {
        etPrivateName.setText(student.getPrivateName());
        etFamilyName.setText(student.getFamilyName());
        etId.setText(student.getId());
        spGrades.setSelection(student.getGrade() - 7);
        etClass.setText("" + student.getClassNum());
        swCanImmune.setChecked(student.getCanImmune());

        vaccinesData[0] = new Vaccine(student.getFirstVaccine());
        vaccinesData[1] = new Vaccine(student.getSecondVaccine());
    }

    /**
     * This function resets the student fields of the activity.
     */
    private void resetStudentFields() {
        etPrivateName.setText("");
        etFamilyName.setText("");
        etId.setText("");
        spGrades.setSelection(0);
        etClass.setText("");
        swCanImmune.setChecked(false);

        vaccinesData[0] = new Vaccine();
        vaccinesData[1] = new Vaccine();
    }

    /**
     * This function resets the vaccines data if needed(depends on the canImmune value).
     * @param canImmune The value of the can immune field.
     */
    private void validateVaccinesSaving(boolean canImmune) {
        if(!canImmune) {
            vaccinesData[0] = null;
            vaccinesData[1] = null;
        }
    }

    /**
     * This function deletes a given student from the DB.
     * @param student The student object to delete.
     */
    private void deleteStudent(Student student) {
        if(student.getCanImmune()) {
            REF_STUDENTS.child("CanImmune").child("" + student.getGrade())
                    .child("" + student.getClassNum()).child(student.getId()).removeValue();
        }
        else {
            REF_STUDENTS.child("CannotImmune").child("" + student.getGrade())
                    .child("" + student.getClassNum()).child(student.getId()).removeValue();
        }
    }

    /**
     * This function deletes an old student object from the DB if some of its fields are different
     * from the new student - its grade, class and id. It's important to do this before adding new
     * student with these different fields.
     * @param oldStudent The object of the old student.
     * @param newStudent The object of the new student.
     */
    private void deleteOldWhenNeeded(Student oldStudent, Student newStudent) {
        if((newStudent.getClassNum() != oldStudent.getClassNum()) ||
                (oldStudent.getGrade() != newStudent.getGrade()) ||
                (!oldStudent.getId().equals(newStudent.getId())) ||
                (!(oldStudent.getCanImmune() == newStudent.getCanImmune())))
        {
            deleteStudent(oldStudent);
        }
    }

    /**
     * This function saves a given student object in the DB.
     * @param student The student object to save.
     */
    private void saveStudInDB(Student student) {
        String canImmune = "CannotImmune";

        if(student.getCanImmune()) {
            canImmune = "CanImmune";
        }

        REF_STUDENTS.child(canImmune).child("" + getSelectedGrade())
                .child(etClass.getText().toString())
                .child(etId.getText().toString()).setValue(student);
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

        editMode = false;
        resetStudentFields();
        tvAddStudent.setText("Add Student");

        if(id == R.id.menuAllStudents){
            si.setClass(this, AllStudentsActivity.class);
            startActivity(si);
        }
        else if(id == R.id.menuSortAndFilter) {
            si.setClass(this, SortAndFilterActivity.class);
            startActivity(si);
        }
        else {
            getSavedIds();
        }

        return super.onOptionsItemSelected(item);
    }
}