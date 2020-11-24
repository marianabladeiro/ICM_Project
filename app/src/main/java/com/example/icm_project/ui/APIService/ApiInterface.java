package com.example.icm_project.ui.APIService;

import com.example.icm_project.ui.model.Course;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("course")
    Call<List<Course>> getCourses();
}
