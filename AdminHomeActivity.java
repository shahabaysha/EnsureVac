package com.ensure.vac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminHomeActivity extends AppCompatActivity {

    private DrawerLayout drawer_layout;
    private Toolbar toolbar;
    private TextView createTeamLabel;
    private TextView addWorkerLabel;
    private TextView settingsLabel;
    private TextView emergencyLabel;
    private TextView logoutLabel;
    private TextView tvWelcomeUser;
    private TextView tvHumidity;
    private TextView tvTemperature;
    private ConstraintLayout clLocations;
    private FirebaseUser fuser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        initUi();
        setupNavigationDrawer();
    }

    private void setupNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        createTeamLabel.setSelected(true);
        addWorkerLabel.setSelected(true);
        settingsLabel.setSelected(true);
        emergencyLabel.setSelected(true);
        logoutLabel.setSelected(true);
    }

    private void initUi() {
        drawer_layout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        createTeamLabel = findViewById(R.id.createTeamLabel);
        addWorkerLabel = findViewById(R.id.addWorkerLabel);
        settingsLabel = findViewById(R.id.settingsLabel);
        emergencyLabel = findViewById(R.id.emergencyLabel);
        logoutLabel = findViewById(R.id.logoutLabel);
        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);

        clLocations = findViewById(R.id.clLocations);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvTemperature = findViewById(R.id.tvTemperature);

        ImageView menuIcon = findViewById(R.id.menuIcon);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Admin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AdminModel admin = dataSnapshot.getValue(AdminModel.class);
                    if (admin.getFullName() != null && !admin.getFullName().isEmpty()) {
                        tvWelcomeUser.setText("Welcome " + admin.getFullName() + "!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        menuIcon.setOnClickListener(view -> {
            menuOpen();
        });

        createTeamLabel.setOnClickListener(view -> {
            menuClose();
            startActivity(new Intent(this, CreateTeamActivity.class));
        });

        addWorkerLabel.setOnClickListener(view -> {
            menuClose();
            startActivity(new Intent(this, AddWorkerActivity.class));
        });

        settingsLabel.setOnClickListener(view -> {
            menuClose();
            startActivity(new Intent(this, AccountSettingsActivity.class));
        });

        emergencyLabel.setOnClickListener(view -> {
            menuClose();
            startActivity(new Intent(this, AdminEmergencyActivity.class));
        });

        logoutLabel.setOnClickListener(view -> {
            menuClose();
            startActivity(new Intent(this, ChooserActivity.class));
            finish();
        });

        clLocations.setOnClickListener(view -> {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("IS_FROM_KEY", "AdminHome");
            startActivity(intent);
        });
    }

    private void menuOpen() {
        if (drawer_layout != null) {
            //check if drawer is already opened
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                //if opened then close it
                drawer_layout.closeDrawer(GravityCompat.START);
            } else {
                //if not opened already, open it now
                drawer_layout.openDrawer(GravityCompat.START);
            }
        }
    }

    private void menuClose() {
        if (drawer_layout != null) {
            //check if drawer is already opened
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                //if opened then close it
                drawer_layout.closeDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final String[] humidityValue = {""};
        final String[] temperatureValue = {""};
        final Boolean[] temperatureAdded = {false};

        reference = FirebaseDatabase.getInstance().getReference("Users").child("Values");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (humidityValue[0].isEmpty()) {
                        humidityValue[0] = dataSnapshot.getValue().toString();
                    }

                    if (temperatureValue[0].isEmpty()) {
                        temperatureValue[0] = dataSnapshot.getValue().toString();

                        if (!temperatureAdded[0]) {
                            temperatureValue[0] = "";
                        }
                        temperatureAdded[0] = true;
                    }
                }

                tvHumidity.setText("Humidity: " + humidityValue[0]);
                tvTemperature.setText("Temperature: " + temperatureValue[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
