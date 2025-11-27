package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display. This must be called before setContentView.
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // This listener handles the system bars (status bar, navigation bar) and applies padding
        // to the main view to prevent content from overlapping with them. This is crucial for EdgeToEdge.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnGoToAddHike = findViewById(R.id.btnGoToAddHike);
        Button btnGoToViewAllHikes = findViewById(R.id.btnGoToViewAllHikes);

        btnGoToAddHike.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddHikeActivity.class);
            startActivity(intent);
        });

        btnGoToViewAllHikes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HikeListActivity.class);
            startActivity(intent);
        });
    }
}
