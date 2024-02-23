package com.example.ambulanceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// UserAdapter.java
// MapPointAdapter.java
public class MapPointAdapter extends RecyclerView.Adapter<MapPointAdapter.MapPointViewHolder> {

    private List<MapPoints> mapPoints = new ArrayList<>();
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }

    public List<MapPoints> getMapPoints() {
        return mapPoints;
    }

    public void setMapPoints(List<MapPoints> mapPoints) {
        this.mapPoints = mapPoints;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MapPointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_point, parent, false);
        return new MapPointViewHolder(view, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MapPointViewHolder holder, int position) {
        MapPoints mapPoint = mapPoints.get(position);
        holder.bind(mapPoint);
    }

    @Override
    public int getItemCount() {
        return mapPoints.size();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    static class MapPointViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewLatitude;
        private TextView textViewLongitude;
        private TextView textViewDividerName;
        private Button buttonDelete;

        public MapPointViewHolder(@NonNull View itemView, OnDeleteClickListener deleteClickListener) {
            super(itemView);

            textViewLatitude = itemView.findViewById(R.id.textViewLatitude);
            textViewLongitude = itemView.findViewById(R.id.textViewLongitude);
            textViewDividerName = itemView.findViewById(R.id.textViewDividerName);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            buttonDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        deleteClickListener.onDeleteClick(position);
                    }
                }
            });
        }

        public void bind(MapPoints mapPoint) {
            // Assuming MapPoints has latitude, longitude, and title fields
            textViewLatitude.setText(String.valueOf(mapPoint.latitude));
            textViewLongitude.setText(String.valueOf(mapPoint.longitude));
            textViewDividerName.setText(mapPoint.title);



        }
    }
}



