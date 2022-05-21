package com.ensure.vac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import javax.security.auth.login.LoginException;

public class CurrentJobActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private RecyclerView rvDescription;
    private ArrayList<String> arrayList;
    private long totalWorkersAdded = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_job);

        NestedScrollView mainLayout = findViewById(R.id.mainLayout);
        TextView etTeamName = findViewById(R.id.etTeamName);
        TextView etJobID = findViewById(R.id.etJobID);
        TextView etJobDes = findViewById(R.id.etJobDes);
        TextView etArea = findViewById(R.id.etArea);
        TextInputEditText etJobDesc = findViewById(R.id.etJobDesc);
        ImageView ivAdd = findViewById(R.id.ivAdd);
        rvDescription = findViewById(R.id.rvDescription);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        DatabaseReference reference;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        final String[] getId = {""};
        arrayList = new ArrayList<>();

        firebaseUser = firebaseAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Users").child("Admin").child("Teams");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.child("addWorkerId")
                            .getValue().toString().contains(firebaseUser.getUid()) &&
                        dataSnapshot.child("workingDates")
                            .getValue().toString().contains(getCurrentDate())
                    ) {
                        mainLayout.setVisibility(View.VISIBLE);

                        etTeamName.setText(String.valueOf(dataSnapshot.child("teamName").getValue()));
                        etJobID.setText(String.valueOf(dataSnapshot.child("teamId").getValue()));
                        etJobDes.setText(String.valueOf(dataSnapshot.child("jobDescription").getValue()));
                        etArea.setText(String.valueOf(dataSnapshot.child("area").getValue()));
                        getId[0] = String.valueOf(dataSnapshot.getKey());
                        totalWorkersAdded = Long.valueOf(String.valueOf(dataSnapshot.child("totalAdded").getValue()));

                        /*if (dataSnapshot.child("Team Members").hasChild(firebaseAuth.getUid())) {
                            Toast.makeText(CurrentJobActivity.this, "No current jobs found", Toast.LENGTH_SHORT).show();
                        }*/

                        if (dataSnapshot.child("Team Members").getChildrenCount() ==
                                totalWorkersAdded
                        ) {
                            DatabaseReference reference =
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child("Admin")
                                            .child("Teams")
                                            .child(getId[0]);
                            reference.child("jobStatus").setValue("Completed");

                            Toast.makeText(CurrentJobActivity.this, "Team has already completed the job. No further jobs found", Toast.LENGTH_SHORT).show();
                            finish();
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

        btnSubmit.setOnClickListener(view -> {
            if (!arrayList.isEmpty()) {

                DatabaseReference ref =
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child("Admin")
                                .child("Teams")
                                .child(getId[0])
                                .child("Team Members")
                                .child(firebaseAuth.getUid());
                ref.child("jobDetail").setValue(
                        arrayList.toString().replace("[", "")
                        .replace("]", "")
                );
            }
        });

        ivAdd.setOnClickListener(view -> {
            if (etJobDesc.getText().toString().length() == 0) {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                arrayList.add(etJobDesc.getText().toString());
                setAdapter();
                etJobDesc.setText("");
            }
        });

        findViewById(R.id.ivBack).setOnClickListener(view -> {
            finish();
        });
    }

    private void  showMessageDialog(
            Context context,
            Activity activity
    ) {
        try {
            if (activity.isFinishing()) return;

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.confirmation_popup, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(false);

            Dialog alertDialog = dialogBuilder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            if (alertDialog != null) {
                if (alertDialog.isShowing()) {
                    return;
                }
            }
            alertDialog.show();


            Button btnYes = dialogView.findViewById(R.id.btnYes);
            Button btnNo = dialogView.findViewById(R.id.btnNo);

            btnYes.setOnClickListener(view -> {
                alertDialog.dismiss();
                resetUI();
            });

            btnNo.setOnClickListener(view -> {
                alertDialog.dismiss();
                finish();
            });
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    private void resetUI() {
        /*etJobID.setText("");
        etJobID.setHint("Job ID");

        etArea.setText("");
        etArea.setHint("Area");

        etJobDes.setText("");
        etJobDes.setHint("Job Description");*/
    }

    private String getCurrentDate() {
        Calendar c = Calendar.getInstance(Locale.US);
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        return sdf.format(c.getTime());
    }

    private void setAdapter() {
        AddDescriptionAdapter addDescriptionAdapter = new AddDescriptionAdapter(
                getApplicationContext(), arrayList);

        rvDescription.setLayoutManager(
                new LinearLayoutManager(
                        getApplicationContext(),
                        RecyclerView.VERTICAL,
                        false
                )
        );
        rvDescription.setAdapter(addDescriptionAdapter);
    }
}