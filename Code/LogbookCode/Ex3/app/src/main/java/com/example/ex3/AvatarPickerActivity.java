package com.example.ex3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.content.Intent;

public class AvatarPickerActivity extends AppCompatActivity {

    ImageView a1, a2, a3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_picker);

        a1 = findViewById(R.id.avatar1);
        a2 = findViewById(R.id.avatar2);
        a3 = findViewById(R.id.avatar3);

        a1.setOnClickListener(v -> choose(R.drawable.ic_avatar1));
        a2.setOnClickListener(v -> choose(R.drawable.ic_avatar2));
        a3.setOnClickListener(v -> choose(R.drawable.ic_avatar3));
    }

    private void choose(int res) {
        Intent result = new Intent();
        result.putExtra("avatar", res);
        setResult(RESULT_OK, result);
        finish();
    }
}
