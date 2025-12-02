package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HikeDetailActivity extends AppCompatActivity implements ObservationAdapter.OnObservationListener {

    public static final String EXTRA_HIKE_ID = "extra_hike_id";

    private DatabaseHelper databaseHelper;
    private Hike hike;
    private int hikeId;

    private TextView detailHikeName, detailHikeLocation, detailHikeDate, detailHikeParking, detailHikeLength, detailHikeDifficulty, detailHikeDescription, detailHikeWeather, detailHikeGroupSize;
    private Button btnAddObservation;
    private RecyclerView recyclerViewObservations;
    private ObservationAdapter observationAdapter;
    private List<Observation> observationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Hike Details");
        }

        databaseHelper = new DatabaseHelper(this);
        initViews();

        hikeId = getIntent().getIntExtra(EXTRA_HIKE_ID, -1);

        if (hikeId != -1) {
            hike = databaseHelper.getHikeById(hikeId);
            if (hike != null) {
                populateHikeDetails();
                setupObservationList();
                setupListeners();
            } else {
                Toast.makeText(this, "Hike not found!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Invalid Hike ID!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hikeId != -1) {
            loadObservations();
        }
    }

    private void initViews() {
        detailHikeName = findViewById(R.id.detail_hike_name);
        detailHikeLocation = findViewById(R.id.detail_hike_location);
        detailHikeDate = findViewById(R.id.detail_hike_date);
        detailHikeParking = findViewById(R.id.detail_hike_parking);
        detailHikeLength = findViewById(R.id.detail_hike_length);
        detailHikeDifficulty = findViewById(R.id.detail_hike_difficulty);
        detailHikeDescription = findViewById(R.id.detail_hike_description);
        detailHikeWeather = findViewById(R.id.detail_hike_weather);
        detailHikeGroupSize = findViewById(R.id.detail_hike_group_size);
        btnAddObservation = findViewById(R.id.btnAddObservation);
        recyclerViewObservations = findViewById(R.id.recyclerViewObservations);
    }

    private void populateHikeDetails() {
        detailHikeName.setText(hike.getName());
        detailHikeLocation.setText("Location: " + hike.getLocation());
        detailHikeDate.setText("Date: " + hike.getDate());
        detailHikeParking.setText("Parking Available: " + (hike.getParkingAvailable() == 1 ? "Yes" : "No"));
        detailHikeLength.setText("Length: " + hike.getLength() + " km");
        detailHikeDifficulty.setText("Difficulty: " + hike.getDifficulty());
        detailHikeDescription.setText("Description: " + hike.getDescription());
        detailHikeWeather.setText("Weather: " + hike.getWeather());
        detailHikeGroupSize.setText("Group Size: " + hike.getGroupSize());
    }

    private void setupObservationList() {
        recyclerViewObservations.setLayoutManager(new LinearLayoutManager(this));
        observationList = new ArrayList<>();
        observationAdapter = new ObservationAdapter(this, observationList, this);
        recyclerViewObservations.setAdapter(observationAdapter);
    }

    private void setupListeners() {
        btnAddObservation.setOnClickListener(v -> {
            Intent intent = new Intent(HikeDetailActivity.this, AddObservationActivity.class);
            intent.putExtra(AddObservationActivity.EXTRA_HIKE_ID, hikeId);
            startActivity(intent);
        });
    }

    private void loadObservations() {
        List<Observation> updatedList = databaseHelper.getObservationsForHike(hikeId);
        observationAdapter.updateData(updatedList);
    }

    @Override
    public void onDeleteClick(Observation observation) {
        databaseHelper.deleteObservation(observation.getId());
        Toast.makeText(this, "Observation deleted", Toast.LENGTH_SHORT).show();
        loadObservations();
    }

    @Override
    public void onEditClick(Observation observation) {
        Intent intent = new Intent(this, EditObservationActivity.class);
        intent.putExtra(EditObservationActivity.EXTRA_OBSERVATION_ID, observation.getId());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
