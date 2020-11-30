package com.example.ICM_Project.ui.model;

public class Student {

    private String nmec;
    private String name;
    private String email;
    private String degree;
    //private ArrayList<String> coursesTimeSpent;

    public Student() {}
    // Getters and setters

    private Student(String nmec, String name, String email, String degree) {
        this.nmec = nmec;
        this.name = name;
        this.email = email;
        this.degree = degree;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getNmec() {
        return nmec;
    }

    public void setNmec(String nmec) {
        this.nmec = nmec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
