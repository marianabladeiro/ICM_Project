package com.example.icm_project;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.icm_project.ui.login.Login;

import java.util.Locale;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Intent login = new Intent(Welcome.this, Login.class);
                    Welcome.this.startActivity(login);
                Welcome.this.finish();
            }
        }, 3000);
    }


}
