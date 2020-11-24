package com.example.icm_project.ui.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.icm_project.MainActivity;
import com.example.icm_project.R;
import com.example.icm_project.databinding.FragmentHomeBinding;
import com.example.icm_project.databinding.ProfileBinding;
import com.example.icm_project.ui.course.CourseFragment;
import com.example.icm_project.ui.model.Course;
import com.example.icm_project.ui.timer.TimerFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/* TODO

use google calendar api to get new event
* */
public class HomeFragment extends Fragment implements View.OnClickListener{


    private ImageButton settings_bt;

    //firebase data
    private TextView nametv;

    //listview for upcoming
    ArrayList<String> up = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter ;

    private CourseAdapter.OnItemClickListener mOnItemClickListener;

    //recyclerview
    private RecyclerView recyclerView;
    List<Course> course;
    CourseAdapter adapter = new CourseAdapter(null, null); // Create Object of the Adapter class

    //avaliar se é preciso usar ou não
    static final String flag = "1";
    String exams;

    private ListView mainListView ; //todo remove


    public HomeFragment() {
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_home, container, false);

        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        settings_bt = binding.settingsButton;
        settings_bt.setOnClickListener( this);
        nametv = binding.nomeTv;
        nametv.setOnClickListener( this);

        //FIREBASE
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query specific_user = reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        specific_user.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        binding.nomeTv.setText(name);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("ERROR", "The read failed");
                    }
                });

        //RECYCLERVIEW COURSES

        //recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        course = new ArrayList<>();

        DatabaseReference coursesRef = FirebaseDatabase.getInstance().getReference("Courses");


        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    for (DataSnapshot d : ds.child("students").getChildren()) {
                        if (d.getValue().toString().equals(uid))  {
                            Course data = ds.getValue(Course.class);
                            course.add(data);

                        }
                    }

                }

                adapter = new CourseAdapter(course, mOnItemClickListener);
                binding.recyclerview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("ERROR", "The read failed");
            }
        });

        return root;
    }


    public static String getFlag() {
        return flag;
    }

    //@Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.nome_tv:
                fragment = new Profile();
                replaceFragment(fragment);
                break;

            case R.id.settings_button:
                fragment = new SettingsFragment();
                replaceFragment(fragment);
                break;
        }

    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.profilefrag, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}