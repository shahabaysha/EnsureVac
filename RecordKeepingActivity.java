package com.ensure.vac;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RecordKeepingActivity extends AppCompatActivity {

    private TextInputEditText etChildName;
    private TextInputEditText etDOB;
    private TextInputEditText etAge;
    private TextInputEditText etChildCNIC;
    private TextInputEditText etFather;
    private TextInputEditText etFatherCNIC;
    private TextInputEditText etvaccineType;
    private TextInputEditText etDose;
    private TextView tvArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_keeping);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        ProgressBar progressBar = findViewById(R.id.progressBar);

        etChildName = findViewById(R.id.etChildName);
        etDOB = findViewById(R.id.etDOB);
        etAge = findViewById(R.id.etAge);
        etChildCNIC = findViewById(R.id.etChildCNIC);
        etFather = findViewById(R.id.etFather);
        etFatherCNIC = findViewById(R.id.etCNIC);
        etvaccineType = findViewById(R.id.etvaccineType);
        etDose = findViewById(R.id.etDose);
        tvArea = findViewById(R.id.tvArea);

        tvArea.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("IS_FROM_KEY", "");
            startActivityForResult(intent, 100);
        });

        findViewById(R.id.btnSubmit).setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            if (etChildName.getText().toString().length() > 0 &&
                    etDOB.getText().toString().length() > 0 &&
                    etAge.getText().toString().length() > 0 &&
                    etChildCNIC.getText().toString().length() > 0 &&
                    etFather.getText().toString().length() > 0 &&
                    etFatherCNIC.getText().toString().length() > 0 &&
                    etvaccineType.getText().toString().length() > 0 &&
                    etDose.getText().toString().length() > 0 &&
                    tvArea.getText().toString().length() > 0) {

                if (!isDateOfBirthValid(etDOB.getText().toString())) {
                    Toast.makeText(RecordKeepingActivity.this, "Date of birth invalid format", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else if ((etvaccineType.getText().toString().equals("Polio") &&
                            Integer.parseInt(etAge.getText().toString()) > 6) ||
                            (etvaccineType.getText().toString().equals("polio") &&
                            Integer.parseInt(etAge.getText().toString()) > 6)
                ) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RecordKeepingActivity.this, "Age cannot be greater than 6 for vaccine type Polio", Toast.LENGTH_SHORT).show();
                }  else if ((etvaccineType.getText().toString().equals("Polio") &&
                            Integer.parseInt(etDose.getText().toString()) > 4) ||
                            (etvaccineType.getText().toString().equals("polio") &&
                            Integer.parseInt(etAge.getText().toString()) > 4)
                ) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RecordKeepingActivity.this, "Dose cannot be greater than 4 for vaccine type Polio", Toast.LENGTH_SHORT).show();
                } else {
    //                startActivity(new Intent(RecordKeepingActivity.this, HomeActivity.class));
    //                finish();
                        //showMessageDialog(getApplicationContext(),this );
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userId = firebaseUser.getUid();

                        DatabaseReference reference =
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child("Workers Child Info")
                                        .child(userId)
                                        .child(String.valueOf(System.currentTimeMillis()));

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("fullName", etChildName.getText().toString());
                        hashMap.put("dateOfBirth", etDOB.getText().toString());
                        hashMap.put("Age", etAge.getText().toString());
                        hashMap.put("ChildCNIC", etChildCNIC.getText().toString());
                        hashMap.put("fatherName", etFather.getText().toString());
                        hashMap.put("FatherCNIC", etFatherCNIC.getText().toString());
                        hashMap.put("vaccineType", etvaccineType.getText().toString());
                        hashMap.put("doseNumber", etDose.getText().toString());
                        hashMap.put("Area", tvArea.getText().toString());

                        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
                            progressBar.setVisibility(View.GONE);
                            showMessageDialog();
                            Toast.makeText(RecordKeepingActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                        });
    //                }
                }
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RecordKeepingActivity.this, "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
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
        }
    }

    private void  showMessageDialog() {
        try {
            if (this.isFinishing()) return;

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.popup_add_more, null);
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


            Button btnNo = dialogView.findViewById(R.id.btnNo);
            Button btnYes = dialogView.findViewById(R.id.btnYes);

            btnYes.setOnClickListener(view -> {
                alertDialog.dismiss();
                clearFields();
            });

            btnNo.setOnClickListener(view -> {
                alertDialog.dismiss();
                finish();
            });
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        etChildName.setText("");
        etDOB.setText("");
        etAge.setText("");
        etChildCNIC.setText("");
        etFather.setText("");
        etFatherCNIC.setText("");
        etvaccineType.setText("");
        etDose.setText("");
        tvArea.setText("");
    }

    private Boolean isDateOfBirthValid(String dateOfBirth) {
        if (dateOfBirth.length() != 10) return false;
        else return String.valueOf(dateOfBirth.charAt(2)).equals("/") &&
                String.valueOf(dateOfBirth.charAt(5)).equals("/");
    }
}

