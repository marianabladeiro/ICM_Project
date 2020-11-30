package com.example.ICM_Project.ui.notes;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ICM_Project.R;
import com.example.ICM_Project.databinding.FragmentNotesBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;

public class NotesFragment extends Fragment {
    Fragment fragment;

    //list to keep notes
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    String m;

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FragmentNotesBinding binding = FragmentNotesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Using custom listView Provided by Android Studio
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, notes);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.notes", getContext().MODE_PRIVATE);
        SharedPreferences.Editor edt = sharedPreferences.edit();
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet(uid, null);

        if (set != null) {
            notes = new ArrayList(set);
        }

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getString("comingFromNotes").equals("this came from course")) {
                m = bundle.getString("innitials");
            }
        }

        binding.listView.setAdapter(arrayAdapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Going from MainActivity to NotesEditorActivity
                fragment = new NewNote();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.notesfrag, fragment);
                transaction.addToBackStack(null);
                Bundle bundle = new Bundle();
                bundle.putInt("noteId", i);
                fragment.setArguments(bundle);
                transaction.commit();

            }
        });

        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;
                // To delete the data from the App
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.notes", getContext().MODE_PRIVATE);
                                HashSet<String> set = new HashSet(NotesFragment.notes);
                                sharedPreferences.edit().putStringSet(uid, set).apply();
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            }
        });


        //add note
        binding.addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("noteId", -1);
                if (m != null) {
                    bundle.putString("ini", m);

                }
                fragment = new NewNote();
                fragment.setArguments(bundle);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.notesfrag, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        return root;
    }

}
