package com.ensure.vac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btnRecordKeeping).setOnClickListener(view -> {

            Intent i = new Intent(getApplicationContext(), RecordKeepingActivity.class);
            startActivity(i);
        });

        findViewById(R.id.btnCurrentJobs).setOnClickListener(view -> {

            Intent i = new Intent(getApplicationContext(), CurrentJobActivity.class);
            startActivity(i);
        });

        findViewById(R.id.btnFutureJobs).setOnClickListener(view -> {

            Intent i = new Intent(getApplicationContext(), FutureJobActivity.class);
            startActivity(i);
        });

        findViewById(R.id.btnPastJobs).setOnClickListener(view -> {

            Intent i = new Intent(getApplicationContext(), PastJobsActivity.class);
            startActivity(i);
        });

        findViewById(R.id.btnLocation).setOnClickListener(view -> {

            Intent i = new Intent(getApplicationContext(), LocationActivity.class);
            startActivity(i);
        });

        findViewById(R.id.btnEmergency).setOnClickListener(view -> {

            Intent i = new Intent(getApplicationContext(), EmergencyActivity.class);
            startActivity(i);
        });

        findViewById(R.id.tvLogout).setOnClickListener(view -> {
            startActivity(new Intent(this, ChooserActivity.class));
            finish();
        });
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
}
