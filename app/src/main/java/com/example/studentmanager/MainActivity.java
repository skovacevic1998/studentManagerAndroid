package com.example.studentmanager;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText studentIdTxt, studentNameTxt, studentEmailTxt;
    Spinner subjectSpinner;
    Button addStudentBtn, viewStudentBtn, deleteStudentBtn, modifyStudentBtn, viewAllStudentsBtn, languageBtnEn, languageBtnHr;
    boolean isSwitchedLanguage = false;
    SQLiteDatabase database;
    List<String> studentSubjectsArray;
    Resources resources;
    Context context;
    ListView listView;
    ArrayList<String> studentList;
    ArrayAdapter<String> listViewAdapter;

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
        languageBtnEn = (Button)findViewById(R.id.change_languageEn);
        languageBtnHr = (Button)findViewById(R.id.change_languageHr);
        listView = (ListView)findViewById(R.id.listView);

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
                    if(isSwitchedLanguage){
                        showMessage("Greška", "Sva polja moraju biti popunjena...");
                    }else{
                        showMessage("Error", "All fields need to be filled...");
                    }

                    return;
                }
                database.execSQL("INSERT INTO student VALUES('"+studentIdTxt.getText()+"', " +
                        "'"+studentNameTxt.getText()+"'," +
                        "'"+studentEmailTxt.getText()+"'," +
                        "'"+subjectSpinner.getSelectedItem().toString()+"')");

                if(isSwitchedLanguage){
                    showMessage("Uspješno", "Uspješno dodan student");
                }else{
                    showMessage("Success", "Student added successfully");
                }

                clearText();
            }
        });

        deleteStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentIdTxt.getText().toString().trim().length()==0){
                    if(isSwitchedLanguage){
                        showMessage("Greška", "Unesite ID studenta...");
                    }else{
                        showMessage("Error", "Please enter student ID...");
                    }
                    return;
                }
                Cursor cursor = database.rawQuery("SELECT * FROM student WHERE studentId='"+studentIdTxt.getText()+"'", null);
                if(cursor.moveToFirst()){
                    database.execSQL("DELETE FROM student WHERE studentId='"+studentIdTxt.getText()+"'");
                    if(isSwitchedLanguage){
                        showMessage("Uspješno", "Student obrisan...");
                    }else{
                        showMessage("Success", "Student deleted...");
                    }
                }else{
                    if(isSwitchedLanguage){
                        showMessage("Greška", "Student ne postoji...");
                    }else{
                        showMessage("Error", "Student does not exist...");
                    }
                }
                clearText();
            }
        });

        modifyStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentIdTxt.getText().toString().trim().length()==0){
                    if(isSwitchedLanguage){
                        showMessage("Greška", "Unesite ID studenta...");
                    }else{
                        showMessage("Error", "Please enter student ID...");
                    }
                    return;
                }
                Cursor cursor = database.rawQuery("SELECT * FROM student WHERE studentId='"+studentIdTxt.getText()+"'", null);
                if(cursor.moveToFirst()){
                    database.execSQL("UPDATE student SET studentName='"
                            +studentNameTxt.getText()+"', studentEmail='"
                            +studentEmailTxt.getText()+"', studentSubject='"
                            +subjectSpinner.getSelectedItem().toString()
                            +"' WHERE studentId='"+studentIdTxt.getText()+"'");
                    if(isSwitchedLanguage){
                        showMessage("Uspješno", "Student sa ID: "+studentIdTxt.getText()+" izmjenjen...");
                    }else{
                        showMessage("Success", "Student with ID: "+studentIdTxt.getText()+" updated...");
                    }

                }else{
                    if(isSwitchedLanguage){
                        showMessage("Greška", "Student ne postoji...");
                    }else{
                        showMessage("Error", "Student does not exist...");
                    }
                }
                clearText();
            }
        });
        viewStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(studentIdTxt.getText().toString().trim().length()==0){
                    if(isSwitchedLanguage){
                        showMessage("Greška", "Unesite ID studenta...");
                    }else{
                        showMessage("Error", "Please enter student ID...");
                    }
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
                    if(isSwitchedLanguage){
                        showMessage("Greška", "ID studenta ne postoji...");
                    }else{
                        showMessage("Error", "Student ID does not exist...");
                    }
                    clearText();
                }
            }
        });

        viewAllStudentsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = database.rawQuery("SELECT * FROM student", null);
                if(cursor.getCount()==0){
                    if(isSwitchedLanguage){
                        showMessage("Greška", "Nema studenata...");
                    }else{
                        showMessage("Error", "No students found...");
                    }
                    return;
                }

                studentList = new ArrayList<>();
                while(cursor.moveToNext()){
                    studentList.add(cursor.getString(0));
                    studentList.add(cursor.getString(1));
                    studentList.add(cursor.getString(2));
                    studentList.add(cursor.getString(3));
                    studentList.add("");
                }

                listViewAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, studentList);
                listView.setAdapter(listViewAdapter);

                listViewAdapter.notifyDataSetChanged();

            }
        });
        languageBtnEn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                context = LocalHelper.setLocale(MainActivity.this, "en");
                resources = context.getResources();

                studentIdTxt.setHint(resources.getString(R.string.studentIdHint));
                studentNameTxt.setHint(resources.getString(R.string.studentNameHint));
                studentEmailTxt.setHint(resources.getString(R.string.studentEmailHint));

                addStudentBtn.setText(resources.getString(R.string.addBtnName));
                deleteStudentBtn.setText(resources.getString(R.string.deleteBtnName));
                modifyStudentBtn.setText(resources.getString(R.string.modifyBtnName));
                viewStudentBtn.setText(resources.getString(R.string.viewBtnName));
                viewAllStudentsBtn.setText(resources.getString(R.string.viewAllBtnName));

                isSwitchedLanguage = false;
            }
        });

        languageBtnHr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context = LocalHelper.setLocale(MainActivity.this, "hr");
                resources = context.getResources();

                studentIdTxt.setHint(resources.getString(R.string.studentIdHint));
                studentNameTxt.setHint(resources.getString(R.string.studentNameHint));
                studentEmailTxt.setHint(resources.getString(R.string.studentEmailHint));

                addStudentBtn.setText(resources.getString(R.string.addBtnName));
                deleteStudentBtn.setText(resources.getString(R.string.deleteBtnName));
                modifyStudentBtn.setText(resources.getString(R.string.modifyBtnName));
                viewStudentBtn.setText(resources.getString(R.string.viewBtnName));
                viewAllStudentsBtn.setText(resources.getString(R.string.viewAllBtnName));

                isSwitchedLanguage = true;
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
        if(studentList.size()!=0){
            listViewAdapter.clear();
            listViewAdapter.notifyDataSetChanged();
        }
        studentIdTxt.requestFocus();
    }
}