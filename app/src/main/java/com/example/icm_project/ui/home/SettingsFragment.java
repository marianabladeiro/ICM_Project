package com.example.icm_project.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.icm_project.MainActivity;
import com.example.icm_project.R;
import com.example.icm_project.databinding.FragmentCourseBinding;
import com.example.icm_project.databinding.SettingsBinding;

import java.util.Locale;


public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsBinding binding = SettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.radioPortugues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setAppLocale("pt");

            }
        });

        binding.radioEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAppLocale("en");

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



    private void setAppLocale(String localeCode) {
        Locale myLocale = new Locale(localeCode);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(getContext(), MainActivity.class);
        getActivity().finish();
        startActivity(refresh);
    }
}
