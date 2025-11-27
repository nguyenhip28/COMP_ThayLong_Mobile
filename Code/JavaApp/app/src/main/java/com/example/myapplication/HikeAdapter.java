package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    private List<Hike> hikeList;
    private Context context;
    private DatabaseHelper databaseHelper;

    public HikeAdapter(Context context, List<Hike> hikeList) {
        this.context = context;
        this.hikeList = hikeList;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hike_list_item, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike hike = hikeList.get(position);
        holder.hikeName.setText(hike.getName());
        holder.hikeLocation.setText("Location: " + hike.getLocation());
        holder.hikeDate.setText("Date: " + hike.getDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HikeDetailActivity.class);
            intent.putExtra(HikeDetailActivity.EXTRA_HIKE_ID, hike.getId());
            context.startActivity(intent);
        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddHikeActivity.class);
            intent.putExtra(AddHikeActivity.EXTRA_HIKE_ID, hike.getId());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            popup.getMenuInflater().inflate(R.menu.hike_list_item_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_delete) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete Hike")
                            .setMessage("Are you sure you want to delete this hike?")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                databaseHelper.deleteHike(hike.getId());
                                updateData(databaseHelper.getAllHikes());
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;
                } else if (itemId == R.id.menu_delete_all) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete All Hikes")
                            .setMessage("Are you sure you want to delete all hikes?")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                databaseHelper.deleteAllHikes();
                                updateData(databaseHelper.getAllHikes());
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;
                }
                return false;
            });
            popup.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    public void updateData(List<Hike> newHikeList) {
        this.hikeList.clear();
        this.hikeList.addAll(newHikeList);
        notifyDataSetChanged();
    }

    static class HikeViewHolder extends RecyclerView.ViewHolder {
        TextView hikeName, hikeLocation, hikeDate;
        Button btnEdit;

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            hikeName = itemView.findViewById(R.id.item_hike_name);
            hikeLocation = itemView.findViewById(R.id.item_hike_location);
            hikeDate = itemView.findViewById(R.id.item_hike_date);
            btnEdit = itemView.findViewById(R.id.btn_edit_hike);
        }
    }
}
