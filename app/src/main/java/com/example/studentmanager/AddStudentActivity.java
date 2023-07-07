package com.example.studentmanager;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {

    EditText studentId, studentName, studentEmail, appNameRegister;
    Spinner subjectSpinner;
    Button registerBtn, changeLanguageEnBtn, changeLanguageHrBtn, backBtn, tryAgainBtn;
    FirebaseFirestore db;
    List<String> studentSubjectsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addstudent_activity);

        studentId = (EditText)findViewById(R.id.student_id);
        studentName = (EditText)findViewById(R.id.student_name);
        studentEmail = (EditText)findViewById(R.id.student_email);
        subjectSpinner = (Spinner)findViewById(R.id.student_subject);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        changeLanguageEnBtn = (Button)findViewById(R.id.change_languageEn);
        changeLanguageHrBtn = (Button)findViewById(R.id.change_languageHr);
        backBtn = (Button)findViewById(R.id.backBtn);
        tryAgainBtn = (Button)findViewById(R.id.tryAgainRegisterBtn);
        appNameRegister = (EditText) findViewById(R.id.appNameRegister);

        studentSubjectsArray = Arrays.asList(getResources().getStringArray(R.array.studentSubjects));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentSubjectsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        subjectSpinner.setAdapter(adapter);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = new Student();
                if(studentId.getText().toString().equals("")){
                    student.setId(0);
                }else{
                    student.setId(Long.valueOf(studentId.getText().toString()));
                }
                student.setStudentName(studentName.getText().toString());
                student.setStudentEmail(studentEmail.getText().toString());
                student.setStudentSubject(subjectSpinner.getSelectedItem().toString());

                if(student.getId() != 0){
                    db = FirebaseFirestore.getInstance();
                    CollectionReference collectionRef = db.collection("studentsData");
                    collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.contains("id") && document.getLong("id").equals(student.getId())) {
                                        Toast.makeText(AddStudentActivity.this, "Student exists!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }else{
                                        if(!(student.getStudentName().isEmpty() && student.getStudentEmail().isEmpty())){
                                            db.collection("studentsData")
                                                    .document() // Generates a unique key for the document
                                                    .set(student)
                                                    .addOnSuccessListener(aVoid -> Toast.makeText(AddStudentActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show())
                                                    .addOnFailureListener(e -> System.out.println("Failed to add student: " + e));
                                            ClearFields();
                                            return;
                                        }else{
                                            Toast.makeText(AddStudentActivity.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                }
                            } else {
                                Exception e = task.getException();
                            }
                        }
                    });
                }else{
                    Toast.makeText(AddStudentActivity.this, "Enter students ID.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        changeLanguageEnBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = LocalHelper.setLocale(AddStudentActivity.this, "en");
                Resources resources = context.getResources();

                registerBtn.setText(resources.getString(R.string.registerBtnName));
                tryAgainBtn.setText(resources.getString(R.string.tryAgainBtnName));
                backBtn.setText(resources.getString(R.string.backBtnName));
                appNameRegister.setText(resources.getString(R.string.appNameAdd));

                studentId.setHint(resources.getString(R.string.studentIdHint));
                studentName.setHint(resources.getString(R.string.studentNameHint));
                studentEmail.setHint(resources.getString(R.string.studentEmailHint));
            }
        });

        changeLanguageHrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = LocalHelper.setLocale(AddStudentActivity.this, "hr");
                Resources resources = context.getResources();

                registerBtn.setText(resources.getString(R.string.registerBtnName));
                tryAgainBtn.setText(resources.getString(R.string.tryAgainBtnName));
                backBtn.setText(resources.getString(R.string.backBtnName));
                appNameRegister.setText(resources.getString(R.string.appNameAdd));

                studentId.setHint(resources.getString(R.string.studentIdHint));
                studentName.setHint(resources.getString(R.string.studentNameHint));
                studentEmail.setHint(resources.getString(R.string.studentEmailHint));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearFields();
            }
        });
    }
    public void ClearFields(){
        studentId.setText("");
        studentName.setText("");
        studentEmail.setText("");
    }
}
