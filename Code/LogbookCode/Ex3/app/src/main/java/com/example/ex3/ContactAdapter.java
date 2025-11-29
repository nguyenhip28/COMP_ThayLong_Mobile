package com.example.ex3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> {

    ArrayList<Contact> list;
    Context context;

    public ContactAdapter(ArrayList<Contact> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder h, int pos) {
        Contact c = list.get(pos);
        h.txtName.setText(c.getName());
        h.txtDob.setText(c.getDob());
        h.txtEmail.setText(c.getEmail());
        h.img.setImageResource(c.getAvatar());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView txtName, txtDob, txtEmail;
        ImageView img;

        public Holder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            txtDob = v.findViewById(R.id.txtDob);
            txtEmail = v.findViewById(R.id.txtEmail);
            img = v.findViewById(R.id.imgAvatar);
        }
    }
}
