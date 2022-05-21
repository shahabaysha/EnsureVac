package com.ensure.vac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

public
class ChooserActivity extends AppCompatActivity {

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        ConstraintLayout clAdmin = findViewById(R.id.clAdmin);
        ConstraintLayout clWorker = findViewById(R.id.clWorker);

        clAdmin.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.putExtra("IS_FROM_KEY", true);
            startActivity(i);
        });

        clWorker.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.putExtra("IS_FROM_KEY", false);
            startActivity(i);
        });


    }
}