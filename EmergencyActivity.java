package com.ensure.vac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EmergencyActivity extends AppCompatActivity {

    private TextInputEditText etTeamName;
    private TextInputEditText etArea;
    private TextInputEditText etIssue;
    private ConstraintLayout mainLayout, clSolution;
    private TextView tvSolution;
    private Button btnSubmit;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        mainLayout = findViewById(R.id.mainLayout);
        clSolution = findViewById(R.id.clSolution);
        etTeamName = findViewById(R.id.etTeamName);
        etArea = findViewById(R.id.etArea);
        etIssue = findViewById(R.id.etIssue);
        tvSolution = findViewById(R.id.tvSolution);
        btnSubmit = findViewById(R.id.btnSubmit);

        final String[] getId = {""};

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Admin").child("Teams");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.child("addWorkerId")
                            .getValue().toString().contains(firebaseUser.getUid()) &&
                        !dataSnapshot.child("jobStatus")
                            .getValue().toString().equals("Completed")
                    ) {
                        mainLayout.setVisibility(View.VISIBLE);

                        etTeamName.setText(String.valueOf(dataSnapshot.child("teamName").getValue()));
                        etArea.setText(String.valueOf(dataSnapshot.child("area").getValue()));
                        getId[0] = String.valueOf(dataSnapshot.getKey());

                        if (!dataSnapshot.child("solution").getValue().toString().isEmpty()) {
                            clSolution.setVisibility(View.VISIBLE);
                            btnSubmit.setVisibility(View.GONE);
                            tvSolution.setText(dataSnapshot.child("solution").getValue().toString());
                        }
                    } else {
//                        mainLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findViewById(R.id.ivBack).setOnClickListener(view -> {
            finish();
        });

        btnSubmit.setOnClickListener(view -> {
            if (etIssue.getText().toString().length() == 0) {
                Toast.makeText(this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                DatabaseReference reference =
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child("Admin")
                                .child("Teams")
                                .child(getId[0]);
                reference.child("issue").setValue(etIssue.getText().toString());

                Toast.makeText(this, "Issue reported to Admin. We will get back to you at the earliest. Thank you", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}