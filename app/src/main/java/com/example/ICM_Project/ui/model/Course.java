package com.example.ICM_Project.ui.model;

import java.util.ArrayList;

public class Course {

    private Long year;
    private Long semester;
    private String name;
    private String responsible;
    private String innitials;
    private String exam;
    //private ArrayList<Student> participants_list;


    public Course() {}
    // Getters and setters

    private Course(String innitials, String name, String responsible, Long semester, Long year, String exam) {
        this.innitials = innitials;
        this.name = name;
        this.responsible = responsible;
        this.semester = semester;
        this.year = year;
        this.exam = exam;

    }


    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getSemester() {
        return semester;
    }
    public void setSemester(Long semester) {this.semester = semester; }


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
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

    public void setInnitials(String innitials) { this.innitials = innitials; }
    public String getInnitials() { return innitials; }

    public void setExam(String exam) { this.exam = exam; }
    public String getExam() { return exam; }
}
