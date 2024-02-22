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
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<Users> users = new ArrayList<>();
    private OnDeleteClickListener onDeleteClickListener; // Added line

    // Added method to set the delete click listener
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }

    public List<Users> getUsers() {
        return users;
    }
    // Interface for delete click listener
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void setUsers(List<Users> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view, onDeleteClickListener); // Passed onDeleteClickListener
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Updated UserViewHolder to include delete button
    static class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserName;
        private TextView textViewUserPassword;
        private Button buttonDelete; // Added line

        public UserViewHolder(@NonNull View itemView, OnDeleteClickListener deleteClickListener) {
            super(itemView);

            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserPassword =itemView.findViewById(R.id.textViewUserPassword);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            // Set up click listener for delete button
            buttonDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        deleteClickListener.onDeleteClick(position);
                    }
                }
            });
        }

        public void bind(Users user) {
            textViewUserName.setText(user.name);
            textViewUserPassword.setText(user.password);
        }
    }
}

