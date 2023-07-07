package com.example.studentmanager;

public class Student {
    long id;
    String studentName;
    String studentEmail;
    String studentSubject;

    public Student(long id, String studentName, String studentEmail, String studentSubject) {
        this.id = id;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentSubject = studentSubject;
    }
    public Student(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentSubject() {
        return studentSubject;
    }

    public void setStudentSubject(String studentSubject) {
        this.studentSubject = studentSubject;
    }
}
