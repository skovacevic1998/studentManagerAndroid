package com.example.studentmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<Student> studentArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<Student> studentArrayList) {
        this.context = context;
        this.studentArrayList = studentArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        Student student = studentArrayList.get(position);

        holder.stId.setText(String.valueOf(student.id));
        holder.stName.setText(student.studentName);
        holder.stEmail.setText(student.studentEmail);
        holder.stSubject.setText(student.studentSubject);
    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView stId, stName, stEmail, stSubject;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stId = itemView.findViewById(R.id.id);
            stName = itemView.findViewById(R.id.studentName);
            stEmail = itemView.findViewById(R.id.studentEmail);
            stSubject = itemView.findViewById(R.id.studentSubject);
        }
    }
}
