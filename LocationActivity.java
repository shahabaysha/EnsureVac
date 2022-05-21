package com.ensure.vac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        findViewById(R.id.btnTeam).setOnClickListener(view -> {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("IS_FROM_KEY", "TEAM");
            startActivity(intent);
        });

        findViewById(R.id.btnPickDrop).setOnClickListener(view -> {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("IS_FROM_KEY", "TEAM");
            startActivity(intent);
        });

        findViewById(R.id.ivBack).setOnClickListener(view -> {
            finish();
        });
    }
}