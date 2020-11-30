package com.example.ICM_Project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ICM_Project.ui.login.Login;

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
