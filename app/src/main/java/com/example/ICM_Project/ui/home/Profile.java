package com.example.ICM_Project.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ICM_Project.R;
import com.example.ICM_Project.Welcome;
import com.example.ICM_Project.databinding.ProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Profile extends Fragment{

    private ArrayAdapter<String> listAdapter ;
    private int totalTimeSpent = 0;

    ArrayList<String> r = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProfileBinding binding = ProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Welcome.class);
                startActivity(intent);

            }
        });


        //FIREBASE
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query specific_user = reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        specific_user.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        binding.nameInfo.setText(name);
                        String degree = dataSnapshot.child("degree").getValue().toString();
                        binding.degreeInfo.setText(degree);
                        String email = dataSnapshot.child("email").getValue().toString();
                        binding.emailInfo.setText(email);
                        String nmec = dataSnapshot.child("nmec").getValue().toString();
                        binding.nmecInfo.setText(nmec);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

        reference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String nameUser = ds.child("name").getValue().toString();

                            for (DataSnapshot d : ds.child("timespent").getChildren()) {
                                totalTimeSpent += d.getValue(Integer.class);

                            }
                            r.add(nameUser + " : " + String.valueOf(totalTimeSpent));

                        }

                        listAdapter = new ArrayAdapter<String>(getContext(), R.layout.rankingrow, r);
                        binding.ratings.setAdapter( listAdapter );

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
            }
        });


        return root;

    }
}
