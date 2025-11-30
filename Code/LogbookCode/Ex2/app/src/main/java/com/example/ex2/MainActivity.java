package com.example.ex2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private MaterialButton btnPrev, btnNext;
    private TextView tvCounter;
    private List<Integer> imageList;

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        imageView = findViewById(R.id.imageViewDisplay);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        tvCounter = findViewById(R.id.tvCounter);

        updateImage();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex < imageList.size() - 1) {
                    currentIndex++;
                    updateImage();
                } else {
                    Toast.makeText(MainActivity.this, "Last image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex > 0) {
                    currentIndex--;
                    updateImage();
                } else {
                    Toast.makeText(MainActivity.this, "First image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        imageList = new ArrayList<>();
        imageList.add(R.drawable.aaa);
        imageList.add(R.drawable.images);
    }

    private void updateImage() {
        imageView.setImageResource(imageList.get(currentIndex));

        String counterText = (currentIndex + 1) + " / " + imageList.size();
        tvCounter.setText(counterText);

        updateButtonState();
    }

    private void updateButtonState() {
        if (currentIndex > 0) {
            btnPrev.setEnabled(true);
            btnPrev.setAlpha(1.0f);
        } else {
            btnPrev.setEnabled(false);
            btnPrev.setAlpha(0.5f);
        }

        if (currentIndex < imageList.size() - 1) {
            btnNext.setEnabled(true);
            btnNext.setAlpha(1.0f);
        } else {
            btnNext.setEnabled(false);
            btnNext.setAlpha(0.5f);
        }
    }
}
