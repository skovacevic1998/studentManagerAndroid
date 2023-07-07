package com.example.studentmanager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DeleteStudentActivity extends AppCompatActivity {
    Button deleteBtn, tryAgainBtn, backBtn, changeLanguageEnBtn, changeLanguageHrBtn;
    TextView studentId, appName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deletestudent_activity);

        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        tryAgainBtn = (Button)findViewById(R.id.tryAgainDeleteBtn);
        backBtn = (Button)findViewById(R.id.backBtn);
        changeLanguageEnBtn = (Button)findViewById(R.id.change_languageEn);
        changeLanguageHrBtn = (Button)findViewById(R.id.change_languageHr);
        studentId = (TextView)findViewById(R.id.student_id);
        appName = (TextView)findViewById(R.id.appNameDelete);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = Long.valueOf(studentId.getText().toString());

                if(id != 0){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference collectionRef = db.collection("studentsData");
                    collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Check if the desired field exists and its value
                                    if (document.contains("id") && document.getLong("id").equals(id)) {
                                        document.getReference().delete();
                                        Toast.makeText(DeleteStudentActivity.this, "Student deleted!", Toast.LENGTH_SHORT).show();
                                        ClearFields();
                                        return;
                                    }else{
                                        Toast.makeText(DeleteStudentActivity.this, "No student found, try different ID.", Toast.LENGTH_SHORT).show();
                                        ClearFields();
                                        return;
                                    }
                                }
                            } else {
                                // Handle any errors
                                Exception e = task.getException();
                                // Log the error or show an error message
                            }
                        }
                    });
                }else{
                    Toast.makeText(DeleteStudentActivity.this, "Enter students ID.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        changeLanguageEnBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = LocalHelper.setLocale(DeleteStudentActivity.this, "en");
                Resources resources = context.getResources();

                deleteBtn.setText(resources.getString(R.string.deleteBtnName));
                tryAgainBtn.setText(resources.getString(R.string.tryAgainBtnName));
                backBtn.setText(resources.getString(R.string.backBtnName));
                appName.setText(resources.getString(R.string.appNameDelete));
                studentId.setHint(resources.getString(R.string.studentIdHint));
            }
        });

        changeLanguageHrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = LocalHelper.setLocale(DeleteStudentActivity.this, "hr");
                Resources resources = context.getResources();

                deleteBtn.setText(resources.getString(R.string.deleteBtnName));
                tryAgainBtn.setText(resources.getString(R.string.tryAgainBtnName));
                backBtn.setText(resources.getString(R.string.backBtnName));
                appName.setText(resources.getString(R.string.appNameDelete));
                studentId.setHint(resources.getString(R.string.studentIdHint));
            }
        });

        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearFields();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void ClearFields(){
        studentId.setText("");
    }
}
