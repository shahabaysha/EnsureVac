package com.ensure.vac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private boolean isFromAdmin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvSignup = findViewById(R.id.tvSignup);
        getIntentData();


        ProgressBar progressBar = findViewById(R.id.progressBar);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (etEmail.getText().toString().length() > 0 &&
                    etPassword.getText().toString().length() > 0) {
                auth.signInWithEmailAndPassword(etEmail.getText().toString(),
                        etPassword.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);

                        Intent intent;
                        if (isFromAdmin) {
                            intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        tvSignup.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
            i.putExtra("IS_FROM_KEY", isFromAdmin);
            startActivity(i);
        });


    }

    private void getIntentData() {
        isFromAdmin =  getIntent().getBooleanExtra("IS_FROM_KEY", false);

    }
}