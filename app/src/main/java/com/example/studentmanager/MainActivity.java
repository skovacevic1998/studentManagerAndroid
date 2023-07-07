package com.example.studentmanager;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addStudentBtn, viewStudentBtn, deleteStudentBtn, modifyStudentBtn, languageBtnEn, languageBtnHr;
    EditText appName;
    LinearLayout linearLayout1;
    Resources resources;
    Context context;
    ConstraintLayout constraintLayoutRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        addStudentBtn = (Button) findViewById(R.id.addstudentbtn);
        deleteStudentBtn = (Button)findViewById(R.id.deletestudentbtn);
        modifyStudentBtn = (Button)findViewById(R.id.modifystudentbtn);
        viewStudentBtn = (Button)findViewById(R.id.viewstudentbtn);
        languageBtnHr = (Button)findViewById(R.id.change_languageHr);
        languageBtnEn = (Button)findViewById(R.id.change_languageEn);
        appName = (EditText)findViewById(R.id.appNameRegister);
        linearLayout1 = (LinearLayout)findViewById(R.id.linearLayout1);
        constraintLayoutRegister = (ConstraintLayout)findViewById(R.id.constraintLayoutMenu);

        languageBtnEn.getBackground().setAlpha(150);
        languageBtnHr.getBackground().setAlpha(150);
        linearLayout1.getBackground().setAlpha(150);
        constraintLayoutRegister.getBackground().setAlpha(200);

        addStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
                startActivity(intent);
            }
        });

        deleteStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteStudentActivity.class);
                startActivity(intent);
            }
        });

        modifyStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ModifyStudentActivity.class);
                startActivity(intent);
            }
        });
        viewStudentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewStudentActivity.class);
                startActivity(intent);
            }
        });

        languageBtnEn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                context = LocalHelper.setLocale(MainActivity.this, "en");
                resources = context.getResources();

                addStudentBtn.setText(resources.getString(R.string.addBtnName));
                deleteStudentBtn.setText(resources.getString(R.string.deleteBtnName));
                modifyStudentBtn.setText(resources.getString(R.string.modifyBtnName));
                viewStudentBtn.setText(resources.getString(R.string.viewBtnName));
                appName.setText(resources.getString(R.string.appNameMenu));
            }
        });

        languageBtnHr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context = LocalHelper.setLocale(MainActivity.this, "hr");
                resources = context.getResources();

                addStudentBtn.setText(resources.getString(R.string.addBtnName));
                deleteStudentBtn.setText(resources.getString(R.string.deleteBtnName));
                modifyStudentBtn.setText(resources.getString(R.string.modifyBtnName));
                viewStudentBtn.setText(resources.getString(R.string.viewBtnName));
                appName.setText(resources.getString(R.string.appNameMenu));
            }
        });
    }
}