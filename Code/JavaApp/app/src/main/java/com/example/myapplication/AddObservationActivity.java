package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    public static final String EXTRA_HIKE_ID = "extra_hike_id";

    private EditText edtObservationText, edtObservationComment;
    private TextView txtObservationTime;
    private Button btnSaveObservation;

    private DatabaseHelper databaseHelper;
    private int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Observation");
        }

        databaseHelper = new DatabaseHelper(this);
        hikeId = getIntent().getIntExtra(EXTRA_HIKE_ID, -1);

        initViews();
        setDefaultTime();

        btnSaveObservation.setOnClickListener(v -> saveObservation());
    }

    private void initViews() {
        edtObservationText = findViewById(R.id.edtObservationText);
        txtObservationTime = findViewById(R.id.txtObservationTime);
        edtObservationComment = findViewById(R.id.edtObservationComment);
        btnSaveObservation = findViewById(R.id.btnSaveObservation);
    }

    private void setDefaultTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        txtObservationTime.setText("Time: " + currentTime);
    }

    private void saveObservation() {
        String observationText = edtObservationText.getText().toString().trim();
        String observationTime = txtObservationTime.getText().toString().replace("Time: ", "").trim();
        String comment = edtObservationComment.getText().toString().trim();

        if (TextUtils.isEmpty(observationText)) {
            edtObservationText.setError("Observation cannot be empty.");
            return;
        }

        if (hikeId == -1) {
            Toast.makeText(this, "Error: Hike ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseHelper.addObservation(hikeId, observationText, observationTime, comment);

        Toast.makeText(this, "Observation saved!", Toast.LENGTH_SHORT).show();
        finish(); // Go back to detail screen
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
