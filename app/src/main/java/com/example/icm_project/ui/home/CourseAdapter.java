package com.example.icm_project.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icm_project.R;
import com.example.icm_project.ui.course.CourseFragment;
import com.example.icm_project.ui.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter {

    //private final ClickInterface listener;
    List<Course> course;

    private OnItemClickListener mOnItemClickListener;

    private Context context;


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public CourseAdapter(List<Course> course, OnItemClickListener onItemClickListener) {
        this.course = course;
        mOnItemClickListener = onItemClickListener;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        context = parent.getContext();

        return viewHolderClass;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;
        Course course1 = course.get(position);
        viewHolderClass.course_name.setText(course1.getName());



       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new CourseFragment();

                //data to pass to CourseFragment
                Course clickedCourse = course.get(holder.getAdapterPosition());

                String nome = clickedCourse.getName();
                String year = clickedCourse.getYear().toString();
                String semester = clickedCourse.getSemester().toString();
                String responsible = clickedCourse.getResponsible();
                String innitials = clickedCourse.getInnitials();
                String exam = clickedCourse.getExam();
                Bundle bundle = new Bundle();

                bundle.putString("nome", nome);
                bundle.putString("year", year);
                bundle.putString("semester", semester);
                bundle.putString("responsible", responsible);
                bundle.putString("innitials", innitials);
                bundle.putString("exam", exam);
                myFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.profilefrag, myFragment).addToBackStack(null).commit();

            }
        });


    }


    @Override
    public int getItemCount() {
        return course.size();
    }


    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView course_name;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            course_name = itemView.findViewById(R.id.name_course);

        }

        public void onClick(View v) {
            Toast.makeText(context, String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }

    }

}

