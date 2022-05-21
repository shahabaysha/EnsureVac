package com.ensure.vac;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.HashMap;

public class CreateTeamActivity extends AppCompatActivity {

    private TextView tvArea;
    private TextView tvAddWorker;
    private ArrayList<AddWorkerModel> addWorkerModelArrayList;
    private DatabaseReference reference;
    private String latitude;
    private String longitude;
    private String workersId = "";
    private int totalWorkersAdded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        TextInputEditText etTeamName = findViewById(R.id.etTeamName);
        TextInputEditText etJobDes = findViewById(R.id.etJobDes);
        TextInputEditText etDate = findViewById(R.id.etDate);
        tvAddWorker = findViewById(R.id.tvAddWorker);
        tvArea = findViewById(R.id.tvArea);
        TextView btnSubmit = findViewById(R.id.btnSubmit);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        tvAddWorker.setOnClickListener(view ->{
            showAddWorkerDialog();
        });

        tvArea.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("IS_FROM_KEY", "");
            startActivityForResult(intent, 100);
        });

        btnSubmit.setOnClickListener(view ->{
            progressBar.setVisibility(View.VISIBLE);
            if (etTeamName.getText().toString().length() == 0 &&
                etJobDes.getText().toString().length() == 0 &&
                tvAddWorker.getText().toString().length() == 0 &&
                tvArea.getText().toString().length() == 0 &&
                etDate.getText().toString().length() == 0) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateTeamActivity.this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (etTeamName.getText().toString().length() > 0 &&
                    tvAddWorker.getText().toString().length() > 0 &&
                    tvArea.getText().toString().length() > 0) {

                FirebaseUser firebaseUser = auth.getCurrentUser();
                assert firebaseUser != null;
//                String teamId = firebaseUser.getUid();
                String teamId = String.valueOf(System.currentTimeMillis());

                DatabaseReference reference =
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child("Admin")
                                .child("Teams")
                                .child(teamId);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("teamId", teamId);
                hashMap.put("teamName", etTeamName.getText().toString());
                hashMap.put("jobDescription", etJobDes.getText().toString());
                hashMap.put("jobStatus", "Pending");
                hashMap.put("workingDates", etDate.getText().toString());
                hashMap.put("addWorker", tvAddWorker.getText().toString());
                hashMap.put("addWorkerId", workersId);
                hashMap.put("totalAdded", String.valueOf(totalWorkersAdded));
                hashMap.put("area", tvArea.getText().toString());
                hashMap.put("latitude", latitude);
                hashMap.put("longitude", longitude);
                hashMap.put("issue", "");
                hashMap.put("solution", "");

                reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                    progressBar.setVisibility(View.GONE);
                    finish();
                    Toast.makeText(CreateTeamActivity.this, "Team added successfully", Toast.LENGTH_SHORT).show();
                });
            }
        });

        findViewById(R.id.ivBack).setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == 100) {
            tvArea.setText(data.getStringExtra("ADDRESS"));
            latitude = data.getStringExtra("LATITUDE");
            longitude = data.getStringExtra("LONGITUDE");
        }
    }

    private void  showAddWorkerDialog() {
        try {
            if (this.isFinishing()) return;

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.popup, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(true);

            Dialog alertDialog = dialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            if (alertDialog != null) {
                if (alertDialog.isShowing()) {
                    return;
                }
            }
            alertDialog.show();

            Button btnSave = dialogView.findViewById(R.id.btnSave);
            TextView tvNoWorkerAdded = dialogView.findViewById(R.id.tvNoWorkerAdded);
            RecyclerView rvAddWorker = dialogView.findViewById(R.id.rvAddWorker);
            final Boolean[] isSelected = {false};
            ArrayList<Integer> mPosition = new ArrayList<>();

            addWorkerModelArrayList = new ArrayList<>();

            reference = FirebaseDatabase.getInstance().getReference("Users").child("Worker");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                        AddWorkerModel user = dataSnapshot.getValue(AddWorkerModel.class);

                        addWorkerModelArrayList.add(new AddWorkerModel(
                                String.valueOf(dataSnapshot.child("dateOfBirth").getValue()),
                                String.valueOf(dataSnapshot.child("fullName").getValue()),
                                String.valueOf(dataSnapshot.child("JobID").getValue()),
                                String.valueOf(dataSnapshot.child("cnic").getValue()),
                                false
                        ));
                    }

                    if (addWorkerModelArrayList.isEmpty()) {
                        tvNoWorkerAdded.setVisibility(View.VISIBLE);
                        rvAddWorker.setVisibility(View.GONE);
                    } else {
                        tvNoWorkerAdded.setVisibility(View.GONE);
                        rvAddWorker.setVisibility(View.VISIBLE);

                        AddWorkerAdapter addWorkerAdapter = new AddWorkerAdapter(
                                getApplicationContext(), addWorkerModelArrayList, new AddWorkerAdapter.IItemClickListener() {
                            @Override
                            public void onItemClick(AddWorkerModel addWorkerModelArrayList, int position) {
                                Log.e("username :", addWorkerModelArrayList.fullName);

                                if (addWorkerModelArrayList.isSelected) {
                                    isSelected[0] = true;
                                    mPosition.add(position);
                                }
                            }
                        });

                        rvAddWorker.setLayoutManager(
                                new LinearLayoutManager(
                                        getApplicationContext(),
                                        RecyclerView.VERTICAL,
                                        false
                                )
                        );
                        rvAddWorker.setAdapter(addWorkerAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            btnSave.setOnClickListener(view -> {
                tvAddWorker.setText("");
                if (tvNoWorkerAdded.getVisibility() == View.VISIBLE) {
                    startActivity(new Intent(this, AddWorkerActivity.class));
                } else {
                    if (isSelected[0]) {
                        if (mPosition.size() == 1) {
                            totalWorkersAdded = 1;
                            tvAddWorker.setText(
                                    addWorkerModelArrayList.get(mPosition.get(0)).getFullName()
                            );
                            workersId = addWorkerModelArrayList.get(mPosition.get(0)).getUserId();
                        } else {
                            for (int position : mPosition) {
                                tvAddWorker.setText(
                                        tvAddWorker.getText().toString().replace("Add Worker", "") +
                                                addWorkerModelArrayList.get(position).getFullName() +
                                                ", "
                                );
                                workersId = workersId + addWorkerModelArrayList.get(position).getUserId() + ", ";
                                totalWorkersAdded = mPosition.size();
                            }
                            int lastIndex;
                            String workersData = tvAddWorker.getText().toString() +
                                    tvAddWorker.getText().toString()
                                            .substring(tvAddWorker.getText().toString().length() - 1)
                                            .replace(", ", "");
                            lastIndex = workersData.lastIndexOf(", ");
                            tvAddWorker.setText(workersData.substring(0, lastIndex));

                            int lastIndex1 = workersId.lastIndexOf(", ");
                            workersId = workersId.substring(0, lastIndex1);
                        }
                        alertDialog.dismiss();
                    }
                }
            });
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}