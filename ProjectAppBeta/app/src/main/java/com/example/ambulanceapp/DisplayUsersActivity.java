package com.example.ambulanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.List;


public class DisplayUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);

        userRepository = new UserRepository(userDatabase.getInstance(getApplicationContext()).userDao()); // Initialize UserRepository

        recyclerView = findViewById(R.id.recyclerView);
        userAdapter = new UserAdapter();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);

        // Set the delete click listener
        userAdapter.setOnDeleteClickListener(position -> deleteUser(position));

        // Load users from the database and update the RecyclerView
        loadUsers();
    }

    private void deleteUser(int position) {
        Users userToDelete = userAdapter.getUsers().get(position);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userRepository.deleteUser(userToDelete);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                // Refresh the RecyclerView after deletion
                loadUsers();
            }
        }.execute();
    }

    private void loadUsers() {
        // Use AsyncTask to perform database operation in the background
        new AsyncTask<Void, Void, List<Users>>() {
            @Override
            protected List<Users> doInBackground(Void... voids) {
                // Retrieve the list of users from the database
                return userRepository.getAllUsers();
            }

            @Override
            protected void onPostExecute(List<Users> users) {
                super.onPostExecute(users);

                // Update the RecyclerView with the list of users
                userAdapter.setUsers(users);
            }
        }.execute();
    }
}
