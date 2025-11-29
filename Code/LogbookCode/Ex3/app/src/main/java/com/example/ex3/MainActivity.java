package com.example.ex3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddContactActivity.class))
        );

        btnView.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ContactListActivity.class))
        );
    }
}
