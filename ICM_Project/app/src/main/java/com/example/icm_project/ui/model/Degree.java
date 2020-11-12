package com.example.icm_project.ui.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Degree {

    @SerializedName("id")
    private String id;

    @SerializedName("director")
    private String director;

    @SerializedName("name")
    private String name;

    @SerializedName("courses")
    private ArrayList<Course> course_list;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Course> getCourse() {
        return course_list;
    }

    public void setCourse_list(ArrayList<Course> course_list) {
        this.course_list = course_list;
    }

}
