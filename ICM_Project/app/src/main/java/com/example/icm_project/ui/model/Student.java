package com.example.icm_project.ui.model;

import com.google.gson.annotations.SerializedName;

public class Student {

    @SerializedName("nmec")
    private String nmec;

    @SerializedName("name")
    private String name;

    // Getters and setters

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
