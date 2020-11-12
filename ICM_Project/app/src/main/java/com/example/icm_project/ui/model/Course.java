package com.example.icm_project.ui.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Course {

    private String year;
    private String semester;
    private String code;
    private String name;
    private String responsible;
    private String innitials;
    private ArrayList<Student> participants_list;

    public Course(int i ) {
        i = i;
        //name = name;
        //year = year;
        //semester = semester;
    }

    // Getters and setters

    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }
    public void setSemester(String semester) {this.semester = semester; }

    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return code;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }
    public String getResponsible() {
        return responsible;
    }

    public void setParticipants(ArrayList<Student> participants_list) {
        this.responsible = responsible;
    }
    public ArrayList<Student> getParticipants() {
        return participants_list;
    }
}
