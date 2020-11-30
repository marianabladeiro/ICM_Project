package com.example.ICM_Project.ui.APIService;

import com.example.ICM_Project.ui.model.Course;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("course")
    Call<List<Course>> getCourses();
}
