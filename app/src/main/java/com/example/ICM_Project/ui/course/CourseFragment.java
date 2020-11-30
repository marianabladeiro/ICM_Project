package com.example.ICM_Project.ui.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ICM_Project.R;
import com.example.ICM_Project.databinding.FragmentCourseBinding;
import com.example.ICM_Project.ui.home.CourseAdapter;
import com.example.ICM_Project.ui.model.Course;
import com.example.ICM_Project.ui.notes.NotesFragment;
import com.example.ICM_Project.ui.timer.TimerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/* TODO

-> statistics: time spent (divide by 60)
* */

public class CourseFragment extends Fragment implements CourseAdapter.OnItemClickListener {

    String nameC, yearC, semesterC, responsibleC, innitials, exam, code;
    List<Course> course;


    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FragmentCourseBinding binding = FragmentCourseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CourseAdapter adapter = new CourseAdapter(null, null);


        //bundle info
        Bundle bundle = this.getArguments();
        nameC = bundle.getString("nome");
        yearC = bundle.getString("year");
        semesterC = bundle.getString("semester");
        responsibleC = bundle.getString("responsible");
        exam = bundle.getString("exam");
        innitials = bundle.getString("innitials");

        binding.nameCourse.setText(nameC);
        binding.courseYear.setText(yearC);
        binding.semesterCourse.setText(semesterC);
        binding.responsibleCourse.setText(responsibleC);
        binding.courseExam.setText(exam);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("timespent").child(nameC);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long time = dataSnapshot.getValue(Long.class);
                if (time != null) {
                    binding.timeSpent.setText(time.toString() + " secs");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        binding.setTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("timercourse", "this came from course");
                bundle.putString("coursename", nameC);
                Fragment fragment = new TimerFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.profilefrag, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        binding.writeNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("comingFromNotes", "this came from course");
                bundle.putString("innitials", innitials);
                Fragment fragment = new NotesFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.profilefrag, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getFragmentManager().getBackStackEntryCount();
                while (count != 0) {
                    getFragmentManager().popBackStack();
                    count = count -1;

                    if (count == 1)  {
                        getActivity().onBackPressed();
                        break;
                    }
                }

            }
        });

        return root;
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
