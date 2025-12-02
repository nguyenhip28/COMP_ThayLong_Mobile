package com.example.ex3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;

public class AddContactActivity extends AppCompatActivity {

    EditText edtName, edtDob, edtEmail;
    ImageView imgAvatar;
    Button btnChoose, btnSave;
    int selectedAvatar = R.drawable.ic_launcher_foreground;

    DBConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        db = new DBConnect(this);

        edtName = findViewById(R.id.edtName);
        edtDob = findViewById(R.id.edtDob);
        edtEmail = findViewById(R.id.edtEmail);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnChoose = findViewById(R.id.btnChooseAvatar);
        btnSave = findViewById(R.id.btnSave);

        btnChoose.setOnClickListener(v -> {
            Intent i = new Intent(AddContactActivity.this, AvatarPickerActivity.class);
            startActivityForResult(i, 111);
        });

        btnSave.setOnClickListener(v -> {
            db.insertContact(new Contact(
                    edtName.getText().toString(),
                    edtDob.getText().toString(),
                    edtEmail.getText().toString(),
                    selectedAvatar
            ));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);

        if (req == 111 && res == RESULT_OK) {
            selectedAvatar = data.getIntExtra("avatar", R.drawable.ic_launcher_foreground);
            imgAvatar.setImageResource(selectedAvatar);
        }
    }
}
