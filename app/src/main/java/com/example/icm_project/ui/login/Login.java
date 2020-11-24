package com.example.icm_project.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.icm_project.MainActivity;
import com.example.icm_project.R;
import com.example.icm_project.databinding.LoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoginBinding binding = LoginBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("LOG_Login", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("LOG_Login", "onAuthStateChanged:signed_out");
                }
            }
        };

        binding.authenticateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!binding.editTextTextEmailAddress.getText().toString().isEmpty() && !binding.editTextTextPassword.getText().toString().isEmpty()) {
                    mAuth.signInWithEmailAndPassword(binding.editTextTextEmailAddress.getText().toString(), binding.editTextTextPassword.getText().toString())
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("LOG_Login", "signInWithEmail:onComplete:" + task.isSuccessful());

                                    if (!task.isSuccessful()) {
                                        Log.w("LOG_Login", "signInWithEmail", task.getException());
                                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        goToMainActivity();
                                    }

                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Authentication failed!", Toast.LENGTH_SHORT).show();
                    binding.editTextTextEmailAddress.setText("");
                    binding.editTextTextPassword.setText("");
                    binding.editTextTextEmailAddress.setHint("Email");
                    binding.editTextTextPassword.setHint("Password");

                }
            }
        });
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
