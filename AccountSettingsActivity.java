package com.ensure.vac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class AccountSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        ImageView ivBack = findViewById(R.id.ivBack);
        EditText etEmail = findViewById(R.id.etEmail);
        Button btnForgotPassword = findViewById(R.id.btnForgotPassword);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        btnForgotPassword.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (etEmail.getText().toString().length() > 0) {
                firebaseAuth.sendPasswordResetEmail(etEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AccountSettingsActivity.this, "Check your email for the reset link", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AccountSettingsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AccountSettingsActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        ivBack.setOnClickListener(view -> {
            finish();
        });
    }
}
