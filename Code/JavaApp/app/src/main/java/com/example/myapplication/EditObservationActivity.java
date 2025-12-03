package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditObservationActivity extends AppCompatActivity {

    public static final String EXTRA_OBSERVATION_ID = "extra_observation_id";

    private EditText edtEditObservationText, edtEditObservationComment;
    private TextView txtEditObservationTime;
    private Button btnUpdateObservation;

    private DatabaseHelper databaseHelper;
    private Observation currentObservation;
    private int observationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_observation);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Observation");
        }

        databaseHelper = new DatabaseHelper(this);
        observationId = getIntent().getIntExtra(EXTRA_OBSERVATION_ID, -1);

        initViews();

        if (observationId != -1) {
            currentObservation = databaseHelper.getObservationById(observationId);
            if (currentObservation != null) {
                populateData();
            } else {
                Toast.makeText(this, "Error: Observation not found.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error: Invalid Observation ID.", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnUpdateObservation.setOnClickListener(v -> updateObservation());
    }

    private void initViews() {
        edtEditObservationText = findViewById(R.id.edtEditObservationText);
        txtEditObservationTime = findViewById(R.id.txtEditObservationTime);
        edtEditObservationComment = findViewById(R.id.edtEditObservationComment);
        btnUpdateObservation = findViewById(R.id.btnUpdateObservation);
    }

    private void populateData() {
        edtEditObservationText.setText(currentObservation.getObservationText());
        txtEditObservationTime.setText("Time: " + currentObservation.getTimeOfObservation());
        edtEditObservationComment.setText(currentObservation.getAdditionalComments());
    }

    private void updateObservation() {
        String newObservationText = edtEditObservationText.getText().toString().trim();
        String newComment = edtEditObservationComment.getText().toString().trim();

        if (TextUtils.isEmpty(newObservationText)) {
            edtEditObservationText.setError("Observation cannot be empty.");
            return;
        }

        databaseHelper.updateObservation(observationId, newObservationText, newComment);
        Toast.makeText(this, "Observation updated!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
