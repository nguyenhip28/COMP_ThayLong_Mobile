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

    // 1. Khai báo các View
    private ImageView imageView;
    private MaterialButton btnPrev, btnNext;
    private TextView tvCounter;

    // 2. Danh sách chứa ID của các ảnh (kiểu Integer)
    private List<Integer> imageList;

    // Biến theo dõi vị trí hiện tại
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 3. Khởi tạo dữ liệu ảnh
        initData();

        // 4. Ánh xạ View (Binding)
        imageView = findViewById(R.id.imageViewDisplay);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        tvCounter = findViewById(R.id.tvCounter);

        // Hiển thị ảnh đầu tiên khi mở app
        updateImage();

        // 5. Xử lý sự kiện nút Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex < imageList.size() - 1) {
                    currentIndex++;
                    updateImage();
                } else {
                    // Thông báo khi đã ở ảnh cuối cùng
                    Toast.makeText(MainActivity.this, "Last image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 6. Xử lý sự kiện nút Previous
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex > 0) {
                    currentIndex--;
                    updateImage();
                } else {
                    // Thông báo khi đã ở ảnh đầu tiên
                    Toast.makeText(MainActivity.this, "First image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm tạo dữ liệu mẫu
    private void initData() {
        imageList = new ArrayList<>();
        // Thay thế các R.drawable.xxx bằng tên ảnh thực tế bạn đã copy vào project
        // Ví dụ: R.drawable.hinh1, R.drawable.hinh2
        imageList.add(R.drawable.aaa); // Ảnh mẫu 1
        imageList.add(R.drawable.images); // Ảnh mẫu 2
        // Thêm tiếp ảnh của bạn vào đây:
        // imageList.add(R.drawable.my_image_1);
        // imageList.add(R.drawable.my_image_2);
    }

    // Hàm cập nhật giao diện theo currentIndex
    private void updateImage() {
        // Set ảnh cho ImageView
        imageView.setImageResource(imageList.get(currentIndex));

        // Cập nhật số trang (Ví dụ: 1 / 5)
        String counterText = (currentIndex + 1) + " / " + imageList.size();
        tvCounter.setText(counterText);

        // Cập nhật trạng thái nút bấm (làm mờ nếu không bấm được)
        updateButtonState();
    }

    private void updateButtonState() {
        // Xử lý nút Prev
        if (currentIndex > 0) {
            btnPrev.setEnabled(true);
            btnPrev.setAlpha(1.0f); // Đậm
        } else {
            btnPrev.setEnabled(false);
            btnPrev.setAlpha(0.5f); // Mờ
        }

        // Xử lý nút Next
        if (currentIndex < imageList.size() - 1) {
            btnNext.setEnabled(true);
            btnNext.setAlpha(1.0f); // Đậm
        } else {
            btnNext.setEnabled(false);
            btnNext.setAlpha(0.5f); // Mờ
        }
    }
}
