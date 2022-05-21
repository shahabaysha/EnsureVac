package com.ensure.vac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddWorkerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);

        EditText etUserName = findViewById(R.id.etUserName);
        EditText etCNIC = findViewById(R.id.etCNIC);
        EditText etDateOfBirth = findViewById(R.id.etDateOfBirth);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnAddWorker = findViewById(R.id.btnAddWorker);
        ImageView ivBack = findViewById(R.id.ivBack);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnAddWorker.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (etUserName.getText().toString().length() == 0 &&
                    etCNIC.getText().toString().length() == 0 &&
                    etDateOfBirth.getText().toString().length() == 0 &&
                    etEmail.getText().toString().length() == 0 &&
                    etPassword.getText().toString().length() == 0) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AddWorkerActivity.this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (!isDateOfBirthValid(etDateOfBirth.getText().toString())) {
                Toast.makeText(AddWorkerActivity.this, "Date of birth invalid format", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else if (etUserName.getText().toString().length() > 0 &&
                    etCNIC.getText().toString().length() > 0 &&
                    etDateOfBirth.getText().toString().length() > 0 &&
                    etEmail.getText().toString().length() > 0 &&
                    etPassword.getText().toString().length() > 0) {
                auth.createUserWithEmailAndPassword(
                        etEmail.getText().toString(),
                        etPassword.getText().toString()
                ).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userId = firebaseUser.getUid();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Worker").child(userId);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("JobID", userId);
                        hashMap.put("fullName", etUserName.getText().toString());
                        hashMap.put("dateOfBirth", etDateOfBirth.getText().toString());
                        hashMap.put("cnic", etCNIC.getText().toString());

                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            progressBar.setVisibility(View.GONE);
                            finish();
                            Toast.makeText(AddWorkerActivity.this, "Worker added successfully", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddWorkerActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ivBack.setOnClickListener(view -> {
            finish();
        });
    }

    private Boolean isDateOfBirthValid(String dateOfBirth) {
        if (dateOfBirth.length() != 10) return false;
        else return String.valueOf(dateOfBirth.charAt(2)).equals("/") &&
                String.valueOf(dateOfBirth.charAt(5)).equals("/");
    }
}
