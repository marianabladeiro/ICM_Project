package com.example.icm_project.ui.calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.icm_project.R;

public class addActivity extends Activity {
    Button button_save, button_cancel;
    EditText name_event_tv;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_addactivity);

        //layout elements
        button_save = findViewById(R.id.button_save);
        button_cancel = findViewById(R.id.button_cancel);
        name_event_tv = findViewById(R.id.name_event_tv);

        button_cancel.setEnabled(true);
        //block button until tittle is not null
        if (name_event_tv.getText().toString() == "") {
            button_save.setEnabled(false);
        } else {
            button_save.setEnabled(true);
        }



    }


    public void click_button_cancel(View view) {
        finish();

    }

    public void save_event(View view) {
        String name_event = name_event_tv.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("name", name_event);
        // set Fragmentclass Arguments
        CalendarFragment fragobj = new CalendarFragment();

        finish();
        
    }
}
