package com.ensure.vac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdminEmergencyActivity extends AppCompatActivity implements EmergencyAdapter.IItemClickListener {

    private EmergencyAdapter emergencyAdapter;
    private ArrayList<EmergencyModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_emergency);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView tvDataFound = findViewById(R.id.tvDataFound);
        RecyclerView rvEmergency = findViewById(R.id.rvEmergency);

        arrayList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Admin").child("Teams");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (!dataSnapshot.child("issue").getValue().toString().isEmpty() &&
                        !dataSnapshot.child("jobStatus")
                            .getValue().toString().equals("Completed") &&
                        dataSnapshot.child("solution").getValue().toString().isEmpty() &&
                        dataSnapshot.child("workingDates").getValue().toString().contains(getCurrentDate())
                    ) {
                        arrayList.add(new EmergencyModel(
                                String.valueOf(dataSnapshot.getKey()),
                                String.valueOf(dataSnapshot.child("issue").getValue()),
                                String.valueOf(dataSnapshot.child("teamName").getValue()),
                                String.valueOf(dataSnapshot.child("addWorker").getValue()),
                                String.valueOf(dataSnapshot.child("area").getValue())
                        ));
                    }

                    if (!arrayList.isEmpty()) {
                        tvDataFound.setVisibility(View.GONE);
                        rvEmergency.setVisibility(View.VISIBLE);

                        emergencyAdapter = new EmergencyAdapter(
                                getApplicationContext(), arrayList, AdminEmergencyActivity.this);

                        rvEmergency.setLayoutManager(
                                new LinearLayoutManager(
                                        getApplicationContext(),
                                        RecyclerView.VERTICAL,
                                        false
                                )
                        );
                        rvEmergency.setAdapter(emergencyAdapter);
                    } else {
                        tvDataFound.setVisibility(View.VISIBLE);
                        rvEmergency.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findViewById(R.id.ivBack).setOnClickListener(view -> {
            finish();
        });
    }

    private String getCurrentDate() {
        Calendar c = Calendar.getInstance(Locale.US);
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        return sdf.format(c.getTime());
    }

    @Override
    public void onItemClick(EmergencyModel emergencyModel, int position, String solution) {
        if (solution.length() == 0) {
            Toast.makeText(AdminEmergencyActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference ref =
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child("Admin")
                            .child("Teams")
                            .child(emergencyModel.getUserId());
            ref.child("solution").setValue(solution);

            Toast.makeText(AdminEmergencyActivity.this, "Solution provided", Toast.LENGTH_SHORT).show();
//            emergencyAdapter.updateUI(position);
            arrayList.clear();
        }
    }
}