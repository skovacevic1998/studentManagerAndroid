package com.example.studentmanager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewStudentActivity extends AppCompatActivity {
    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;
    Button backBtn, changeLanguageHrBtn, changeLanguageEnBtn;
    TextView appNameView;
    ArrayList<Student> studentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewstudent_activity);

        appNameView = (TextView)findViewById(R.id.appNameView);
        changeLanguageEnBtn = (Button)findViewById(R.id.change_languageEn);
        changeLanguageHrBtn = (Button)findViewById(R.id.change_languageHr);
        backBtn = (Button)findViewById(R.id.backBtn);
        recyclerView = (RecyclerView)findViewById(R.id.studentRecyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentArrayList = new ArrayList<Student>();

        recyclerViewAdapter = new RecyclerViewAdapter(ViewStudentActivity.this, studentArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

        EventChangeListener();

        changeLanguageEnBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = LocalHelper.setLocale(ViewStudentActivity.this, "en");
                Resources resources = context.getResources();

                backBtn.setText(resources.getString(R.string.backBtnName));
                appNameView.setText(resources.getString(R.string.appNameView));
            }
        });

        changeLanguageHrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = LocalHelper.setLocale(ViewStudentActivity.this, "hr");
                Resources resources = context.getResources();

                backBtn.setText(resources.getString(R.string.backBtnName));
                appNameView.setText(resources.getString(R.string.appNameView));
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

    private void EventChangeListener() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("studentsData").orderBy("id", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Firestore error", error.getMessage());
                    return;
                }

                if(!value.getDocumentChanges().isEmpty()){
                    for(DocumentChange dc: value.getDocumentChanges()){
                        Student student = dc.getDocument().toObject(Student.class);

                        if(dc.getType()== DocumentChange.Type.ADDED){
                            studentArrayList.add(dc.getDocument().toObject(Student.class));
                        }

                        if(dc.getType() == DocumentChange.Type.MODIFIED){
                            studentArrayList.set(dc.getOldIndex(), student);
                        }

                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }else Toast.makeText(ViewStudentActivity.this, "No students found!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
