package com.example.icm_project.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icm_project.R;
import com.example.icm_project.ui.model.Course;
import com.example.icm_project.ui.model.Degree;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/* TODO
RecyclerView data Firebase
check internet connection
data binding
use google calendar api to get new event
* */
public class HomeFragment extends Fragment  {

    //layout
    private TextView name_course;

    private RecyclerView recyclerView;

    ArrayList<String> courseInnitials;
    //firebase data
    private String name;
    private String responsible;

    private HomeViewModel homeViewModel;

    private CardView cardview;
    private List<Degree> movies = new ArrayList<>();
    private DegreeViewHolder courseAdapter;

    //avaliar se é preciso usar ou não
    static final String flag = "1";



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        name_course = (TextView) root.findViewById(R.id.name_course);

        //firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {

                    String name = datas.child("name").getValue().toString();
                    //courseInnitials.add(name);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // set up the RecyclerView
        ArrayList <Course> itemList = new ArrayList<Course>();

        DegreeAdapter itemArrayAdapter = new DegreeAdapter(R.layout.item_course, itemList);

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);

        // Populating list items
        for(int i = 0; i <= 9; i++) {
            itemList.add(new Course(i));
        }

        return root;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static String getFlag() {
        return flag;
    }


    /*private void getDegree(){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Degree>> call = apiInterface.getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if(response.isSuccessful()) {
                    for(Movie movie: response.body()){
                        movies.add(movie);
                    }
                    moviesAdapter.notifyDataSetChanged();
                }else{
                    Log.e(TAG, response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    } */
}