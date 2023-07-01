package com.example.studentmanager;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.view.View.OnClickListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText studentIdTxt, studentNameTxt, studentEmailTxt;
    Spinner subjectSpinner;
    Button addStudentBtn, viewStudentBtn, deleteStudentBtn, modifyStudentBtn, viewAllStudentsBtn;
    ImageButton languageBtn;
    SQLiteDatabase database;
    List<String> studentSubjectsArray;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentIdTxt = (EditText)findViewById(R.id.student_id);
        studentNameTxt = (EditText)findViewById(R.id.student_name);
        studentEmailTxt = (EditText)findViewById(R.id.student_email);
        subjectSpinner = (Spinner)findViewById(R.id.subject_spinner);
        addStudentBtn = (Button)findViewById(R.id.addstudentbtn);
        deleteStudentBtn = (Button)findViewById(R.id.deletestudentbtn);
        modifyStudentBtn = (Button)findViewById(R.id.modifystudentbtn);
        viewStudentBtn = (Button)findViewById(R.id.viewstudentbtn);
        viewAllStudentsBtn = (Button)findViewById(R.id.viewallstudentsbtn);
        languageBtn = (ImageButton)findViewById(R.id.change_language);

        database = openOrCreateDatabase("students", Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS student(studentId INTEGER, studentName VARCHAR, studentEmail VARCHAR, studentSubject VARCHAR)");

        studentSubjectsArray = Arrays.asList(getResources().getStringArray(R.array.studentSubjects));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentSubjectsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        subjectSpinner.setAdapter(adapter);

        addStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentIdTxt.getText().toString().trim().length() == 0
                        || studentNameTxt.getText().toString().trim().length() == 0
                        || studentEmailTxt.getText().toString().trim().length() == 0
                        || subjectSpinner.getSelectedItem().toString().trim().length() == 0){
                    showMessage("Error", "All fields need to be filled...");
                    return;
                }
                database.execSQL("INSERT INTO student VALUES('"+studentIdTxt.getText()+"', " +
                        "'"+studentNameTxt.getText()+"'," +
                        "'"+studentEmailTxt.getText()+"'," +
                        "'"+subjectSpinner.getSelectedItem().toString()+"')");

                showMessage("Success", "Student added successfully");
                clearText();
            }
        });

        deleteStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentIdTxt.getText().toString().trim().length()==0){
                    showMessage("Error", "Please enter student ID...");
                    return;
                }
                Cursor cursor = database.rawQuery("SELECT * FROM student WHERE studentId='"+studentIdTxt.getText()+"'", null);
                if(cursor.moveToFirst()){
                    database.execSQL("DELETE FROM student WHERE studentId='"+studentIdTxt.getText()+"'");
                    showMessage("Success", "Student deleted...");
                }else{
                    showMessage("Error", "Student does not exist!");
                }
                clearText();
            }
        });

        modifyStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentIdTxt.getText().toString().trim().length()==0){
                    showMessage("Error", "Please enter student ID...");
                    return;
                }
                Cursor cursor = database.rawQuery("SELECT * FROM student WHERE studentId='"+studentIdTxt.getText()+"'", null);
                if(cursor.moveToFirst()){
                    database.execSQL("UPDATE student SET studentName='"
                            +studentNameTxt.getText()+"', studentEmail='"
                            +studentEmailTxt.getText()+"', studentSubject='"
                            +subjectSpinner.getSelectedItem().toString()
                            +"' WHERE studentId='"+studentIdTxt.getText()+"'");
                    showMessage("Success", "Student with ID: "+studentIdTxt.getText()+" updated...");
                }else{
                    showMessage("Error", "Student ID does not exist...");
                }
                clearText();
            }
        });
        viewStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentIdTxt.getText().toString().trim().length()==0)
                {
                    showMessage("Error", "Please enter student ID...");
                    return;
                }
                Cursor cursor = database.rawQuery("SELECT * FROM student WHERE studentId='"+studentIdTxt.getText()+"'", null);
                if(cursor.moveToFirst()){
                    studentNameTxt.setText(cursor.getString(1));
                    studentEmailTxt.setText(cursor.getString(2));

                    if("Računarstvo".equals(cursor.getString(3))){
                        subjectSpinner.setSelection(adapter.getPosition("Računarstvo"));
                    }else if("Mehatronika".equals(cursor.getString(3))){
                        subjectSpinner.setSelection(adapter.getPosition("Mehatronika"));
                    }else if("Sestrinstvo".equals(cursor.getString(3))){
                        subjectSpinner.setSelection(adapter.getPosition("Sestrinstvo"));
                    }
                }else{
                    showMessage("Error", "Student ID does not exist...");
                    clearText();
                }
            }
        });

        viewAllStudentsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = database.rawQuery("SELECT * FROM student", null);
                if(cursor.getCount()==0){
                    showMessage("Error", "No students found...");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(cursor.moveToNext()){
                    buffer.append("studentId: "+cursor.getString(0)+"\n");
                    buffer.append("studentName: "+cursor.getString(1)+"\n");
                    buffer.append("studentEmail: "+cursor.getString(2)+"\n");
                    buffer.append("studentSubject: "+cursor.getString(3)+"\n\n\n");
                }
                showMessage("Student Details", buffer.toString());
            }
        });
    }



























    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        studentIdTxt.setText("");
        studentNameTxt.setText("");
        studentEmailTxt.setText("");
        studentIdTxt.requestFocus();
    }
}