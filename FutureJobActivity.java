package com.ensure.vac;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FutureJobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_job);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView tvDataFound = findViewById(R.id.tvDataFound);
        RecyclerView rvViewDetail = findViewById(R.id.rvViewDetail);

        progressBar.setVisibility(View.VISIBLE);

        ArrayList<JobDetailModel> jobDetailModelArrayList = new ArrayList<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users").child("Admin").child("Teams");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                    if (dataSnapshot.child("addWorkerId").getValue().toString()
                            .contains(firebaseUser.getUid()) &&
                        dataSnapshot.child("jobStatus").getValue().toString().equals("Pending")
                    ) {
                        jobDetailModelArrayList.add(new JobDetailModel(
                                String.valueOf(dataSnapshot.child("teamName").getValue()),
                                String.valueOf(dataSnapshot.child("jobDescription").getValue()),
                                String.valueOf(dataSnapshot.child("workingDates").getValue()),
                                String.valueOf(dataSnapshot.child("addWorker").getValue()),
                                String.valueOf(dataSnapshot.child("area").getValue())
                        ));
                    }
                }

                if (jobDetailModelArrayList.isEmpty()) {
                    tvDataFound.setVisibility(View.VISIBLE);
                    rvViewDetail.setVisibility(View.GONE);
                } else {
                    tvDataFound.setVisibility(View.GONE);
                    rvViewDetail.setVisibility(View.VISIBLE);

                    JobDetailAdapter addWorkerAdapter = new JobDetailAdapter(
                            getApplicationContext(), jobDetailModelArrayList);

                    rvViewDetail.setLayoutManager(
                            new LinearLayoutManager(
                                    getApplicationContext(),
                                    RecyclerView.VERTICAL,
                                    false
                            )
                    );
                    rvViewDetail.setAdapter(addWorkerAdapter);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        findViewById(R.id.ivBack).setOnClickListener(view -> {
            finish();
        });
    }
}
