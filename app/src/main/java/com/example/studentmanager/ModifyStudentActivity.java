package com.example.studentmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class ModifyStudentActivity extends AppCompatActivity {
    EditText studentId, studentName, studentEmail, appNameModify;
    Spinner studentSubjectSpinner;
    Button getStudentBtn, modifyStudentBtn, tryAgainStudentBtn, backBtn, changeLanguageEnBtn, changeLanguageHrBtn;
    FirebaseFirestore db;
    boolean isStudentSet;
    Student student;
    String documentId;
    List<String> studentSubjectsArray;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifystudent_activity);

        studentId = (EditText)findViewById(R.id.student_id_modify);
        studentName = (EditText)findViewById(R.id.student_name);
        studentEmail = (EditText)findViewById(R.id.student_email);
        studentSubjectSpinner = (Spinner)findViewById(R.id.student_subject);
        getStudentBtn = (Button)findViewById(R.id.getStudentBtn);
        modifyStudentBtn = (Button)findViewById(R.id.modifyBtn);
        tryAgainStudentBtn = (Button)findViewById(R.id.tryAgainModifyBtn);
        backBtn = (Button)findViewById(R.id.backBtn);
        changeLanguageEnBtn = (Button)findViewById(R.id.change_languageEn);
        changeLanguageHrBtn = (Button)findViewById(R.id.change_languageHr);
        appNameModify = (EditText)findViewById(R.id.appNameModify);

        studentSubjectsArray = Arrays.asList(getResources().getStringArray(R.array.studentSubjects));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentSubjectsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        studentSubjectSpinner.setAdapter(adapter);

        if(!isStudentSet){
            studentId.setEnabled(true);
            studentId.requestFocus();
        }

        getStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student = new Student();
                if(studentId.getText().toString().equals("")){
                    student.setId(0);
                }else{
                    student.setId(Long.valueOf(studentId.getText().toString()));
                }

                if(student.getId() != 0){
                    documentId = new String();
                    db = FirebaseFirestore.getInstance();
                    CollectionReference collectionRef = db.collection("studentsData");
                    collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                isStudentSet = false;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.contains("id") && document.getLong("id").equals(student.getId())) {
                                        student.setStudentName(document.getString("studentName").toString());
                                        student.setStudentEmail(document.getString("studentEmail").toString());
                                        student.setStudentSubject(document.getString("studentSubject").toString());
                                        documentId = document.getId().toString();

                                        studentId.setText(String.valueOf(student.getId()));
                                        studentName.setText(student.getStudentName());
                                        studentEmail.setText(student.getStudentEmail());
                                        studentId.setEnabled(false);

                                        int backgroundColor = getResources().getColor(R.color.gray);
                                        studentId.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));

                                        int index = -1;
                                        for(int i=0; i<studentSubjectsArray .size(); i++){
                                            if(studentSubjectsArray.get(i).equals(student.getStudentSubject())){
                                                index = i;
                                                break;
                                            }

                                        }

                                        studentSubjectSpinner.setSelection(index);

                                        isStudentSet = true;
                                        return;
                                    }else{
                                        Toast.makeText(ModifyStudentActivity.this, "Student not found!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            } else {
                                Exception e = task.getException();
                            }
                        }
                    });
                }else{
                    Toast.makeText(ModifyStudentActivity.this, "Enter students ID.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        modifyStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStudentSet){
                    student.setStudentName(studentName.getText().toString());
                    student.setStudentEmail(studentEmail.getText().toString());
                    student.setStudentSubject(studentSubjectSpinner.getSelectedItem().toString());

                    db.collection("studentsData")
                            .document(documentId)
                            .set(student)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ModifyStudentActivity.this, "Student updated!", Toast.LENGTH_SHORT).show();
                                    int backgroundColor = getResources().getColor(R.color.white);
                                    studentId.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
                                    isStudentSet = false;
                                    ClearFields();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ModifyStudentActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

        changeLanguageEnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = LocalHelper.setLocale(ModifyStudentActivity.this, "en");
                Resources resources = context.getResources();

                getStudentBtn.setText(resources.getString(R.string.getStudentBtnName));
                modifyStudentBtn.setText(resources.getString(R.string.modifyBtnName));
                tryAgainStudentBtn.setText(resources.getString(R.string.tryAgainBtnName));
                backBtn.setText(resources.getString(R.string.backBtnName));

                appNameModify.setText(resources.getString(R.string.appNameModify));

                studentId.setHint(resources.getString(R.string.studentIdHint));
                studentName.setHint(resources.getString(R.string.studentNameHint));
                studentEmail.setHint(resources.getString(R.string.studentEmailHint));
            }
        });

        changeLanguageHrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = LocalHelper.setLocale(ModifyStudentActivity.this, "hr");
                Resources resources = context.getResources();

                getStudentBtn.setText(resources.getString(R.string.getStudentBtnName));
                modifyStudentBtn.setText(resources.getString(R.string.modifyBtnName));
                tryAgainStudentBtn.setText(resources.getString(R.string.tryAgainBtnName));
                backBtn.setText(resources.getString(R.string.backBtnName));

                appNameModify.setText(resources.getString(R.string.appNameModify));

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

        tryAgainStudentBtn.setOnClickListener(new View.OnClickListener() {
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
        int backgroundColor = getResources().getColor(R.color.white);
        studentId.setEnabled(true);
        studentId.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));
        studentId.requestFocus();
        studentSubjectSpinner.setSelection(0);
    }
}
