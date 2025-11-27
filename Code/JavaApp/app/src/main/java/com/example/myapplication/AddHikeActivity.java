package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddHikeActivity extends AppCompatActivity {

    public static final String EXTRA_HIKE_ID = "com.example.myapplication.EXTRA_HIKE_ID";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private TextInputEditText edtName, edtLocation, edtDate, edtLength, edtDescription;
    private TextInputLayout nameLayout, locationLayout, dateLayout, lengthLayout;
    private AutoCompleteTextView spinnerDifficulty;
    private RadioGroup radioParking;
    private Button btnSubmit, btnGetLocation;
    private TextView txtFormTitle;
    private DatabaseHelper databaseHelper;
    private int hikeId = -1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        databaseHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupViews();
        setupDifficultyDropdown();
        setupListeners();

        if (getIntent().hasExtra(EXTRA_HIKE_ID)) {
            hikeId = getIntent().getIntExtra(EXTRA_HIKE_ID, -1);
            if (hikeId != -1) {
                setTitle(R.string.form_title_edit_hike);
                txtFormTitle.setText(R.string.form_title_edit_hike);
                loadHikeData();
            } else {
                setTitle(R.string.form_title_add_hike);
                txtFormTitle.setText(R.string.form_title_add_hike);
            }
        } else {
            setTitle(R.string.form_title_add_hike);
            txtFormTitle.setText(R.string.form_title_add_hike);
        }
    }

    private void loadHikeData() {
        Hike hike = databaseHelper.getHikeById(hikeId);
        if (hike != null) {
            edtName.setText(hike.getName());
            edtLocation.setText(hike.getLocation());
            edtDate.setText(hike.getDate());
            edtLength.setText(String.valueOf(hike.getLength()));
            if (hike.getParkingAvailable() == 1) {
                radioParking.check(R.id.rdoYes);
            } else {
                radioParking.check(R.id.rdoNo);
            }
            spinnerDifficulty.setText(hike.getDifficulty(), false);
            edtDescription.setText(hike.getDescription());
        }
    }

    private void setupViews() {
        txtFormTitle = findViewById(R.id.txtFormTitle);
        nameLayout = findViewById(R.id.name_layout);
        locationLayout = findViewById(R.id.location_layout);
        dateLayout = findViewById(R.id.date_layout);
        lengthLayout = findViewById(R.id.length_layout);

        edtName = findViewById(R.id.edtName);
        edtLocation = findViewById(R.id.edtLocation);
        edtDate = findViewById(R.id.edtDate);
        edtLength = findViewById(R.id.edtLength);
        radioParking = findViewById(R.id.radioParking);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        edtDescription = findViewById(R.id.edtDescription);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnGetLocation = findViewById(R.id.btnGetLocation);
    }

    private void setupDifficultyDropdown() {
        String[] difficultyLevels = getResources().getStringArray(R.array.difficulty_levels);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, difficultyLevels);
        spinnerDifficulty.setAdapter(adapter);
    }

    private void setupListeners() {
        edtDate.setOnClickListener(v -> showDatePickerDialog());
        btnSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                showConfirmationDialog();
            }
        });
        btnGetLocation.setOnClickListener(v -> getCurrentLocation());
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        edtLocation.setText(String.format(Locale.getDefault(), "%f, %f", location.getLatitude(), location.getLongitude()));
                    } else {
                        Toast.makeText(this, "Unable to get location.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs() {
        nameLayout.setError(null);
        locationLayout.setError(null);
        dateLayout.setError(null);
        lengthLayout.setError(null);

        String name = edtName.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();
        String date = edtDate.getText().toString().trim();
        String length = edtLength.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameLayout.setError("Name is required.");
            return false;
        }

        if (TextUtils.isEmpty(location)) {
            locationLayout.setError("Location is required.");
            return false;
        }

        if (TextUtils.isEmpty(date)) {
            dateLayout.setError("Date is required.");
            return false;
        }

        if (radioParking.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a parking option.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(length)) {
            lengthLayout.setError("Length is required.");
            return false;
        }

        return true;
    }

    private void showConfirmationDialog() {
        String name = edtName.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();
        String date = edtDate.getText().toString().trim();
        String lengthStr = edtLength.getText().toString().trim();
        String difficulty = spinnerDifficulty.getText().toString();
        String description = edtDescription.getText().toString().trim();

        boolean parkingAvailable = radioParking.getCheckedRadioButtonId() == R.id.rdoYes;
        String parking = parkingAvailable ? "Yes" : "No";

        String confirmationMessage = "Please confirm the details:\n\n" +
                "Name: " + name + "\n" +
                "Location: " + location + "\n" +
                "Date: " + date + "\n" +
                "Parking: " + parking + "\n" +
                "Length: " + lengthStr + " km\n" +
                "Difficulty: " + difficulty + "\n" +
                "Description: " + (!description.isEmpty() ? description : "N/A");

        new AlertDialog.Builder(this)
                .setTitle("Confirm Hike Details")
                .setMessage(confirmationMessage)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    saveHikeAndNavigate();
                })
                .setNegativeButton("Go Back", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create()
                .show();
    }

    private void saveHikeAndNavigate() {
        String name = edtName.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();
        String date = edtDate.getText().toString().trim();
        double length = Double.parseDouble(edtLength.getText().toString().trim());
        int parking = radioParking.getCheckedRadioButtonId() == R.id.rdoYes ? 1 : 0;
        String difficulty = spinnerDifficulty.getText().toString();
        String description = edtDescription.getText().toString().trim();
        String weather = ""; // Not in layout
        int groupSize = 0; // Not in layout

        if (hikeId == -1) {
            databaseHelper.addHike(name, location, date, parking, length, difficulty, description, weather, groupSize);
            Toast.makeText(this, "Hike saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            databaseHelper.updateHike(hikeId, name, location, date, parking, length, difficulty, description, weather, groupSize);
            Toast.makeText(this, "Hike updated successfully!", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(AddHikeActivity.this, HikeListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year1, monthOfYear, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    edtDate.setText(sdf.format(newDate.getTime()));
                }, year, month, day).show();
    }
}
