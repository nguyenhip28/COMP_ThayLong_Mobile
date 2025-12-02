package com.example.ex3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DBConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        db = new DBConnect(this);
        recyclerView = findViewById(R.id.recyclerContacts);

        ArrayList<Contact> list = db.getAllContacts();
        ContactAdapter adapter = new ContactAdapter(list, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
