package com.example.icm_project.ui.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.icm_project.R;
import com.example.icm_project.databinding.FragmentCourseBinding;
import com.example.icm_project.databinding.NewNoteBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashSet;

public class NewNote extends Fragment {
    int noteId;
    Fragment fragment;
    String m, n;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewNoteBinding binding = NewNoteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            binding.textView13.setText(bundle.getString("ini"));
        }

        // Accessing the data using key and value
        noteId = bundle.getInt("noteId", -1);

        if (noteId != -1) {
            binding.editText.setText(NotesFragment.notes.get(noteId));
        } else {

            NotesFragment.notes.add("");
            noteId = NotesFragment.notes.size() - 1;
            NotesFragment.arrayAdapter.notifyDataSetChanged();

        }



        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                NotesFragment.notes.set(noteId, String.valueOf(charSequence));
                NotesFragment.arrayAdapter.notifyDataSetChanged();

                    // Creating Object of SharedPreferences to store data in the phone
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.notes", getContext().MODE_PRIVATE);
                    HashSet<String> set = new HashSet(NotesFragment.notes);
                    sharedPreferences.edit().putStringSet(uid, set).apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editText.getText().toString().isEmpty()) {
                    binding.save.setEnabled(false);
                }
                if (!binding.editText.getText().toString().isEmpty()) {
                    binding.save.setEnabled(true);
                }
            }
        });


        binding.save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
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

        binding.btCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        //access camera
        binding.camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });

        return root;
    }

}
