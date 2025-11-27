package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {

    private List<Observation> observationList;
    private Context context;
    private OnObservationListener listener;

    public interface OnObservationListener {
        void onDeleteClick(Observation observation);
        void onEditClick(Observation observation);
    }

    public ObservationAdapter(Context context, List<Observation> observationList, OnObservationListener listener) {
        this.context = context;
        this.observationList = observationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.observation_list_item, parent, false);
        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation observation = observationList.get(position);
        holder.observationText.setText(observation.getObservationText());
        holder.observationTime.setText("Time: " + observation.getTimeOfObservation());

        String comment = observation.getAdditionalComments();
        if (comment != null && !comment.isEmpty()) {
            holder.observationComment.setText("Comment: " + comment);
            holder.observationComment.setVisibility(View.VISIBLE);
        } else {
            holder.observationComment.setVisibility(View.GONE);
        }

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(observation);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Observation")
                    .setMessage("Are you sure you want to delete this observation?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (listener != null) {
                            listener.onDeleteClick(observation);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }

    public void updateData(List<Observation> newObservationList) {
        this.observationList.clear();
        this.observationList.addAll(newObservationList);
        notifyDataSetChanged();
    }

    static class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView observationText, observationTime, observationComment;
        ImageButton editButton, deleteButton;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            observationText = itemView.findViewById(R.id.item_observation_text);
            observationTime = itemView.findViewById(R.id.item_observation_time);
            observationComment = itemView.findViewById(R.id.item_observation_comment);
            editButton = itemView.findViewById(R.id.btnEditObservation);
            deleteButton = itemView.findViewById(R.id.btnDeleteObservation);
        }
    }
}
