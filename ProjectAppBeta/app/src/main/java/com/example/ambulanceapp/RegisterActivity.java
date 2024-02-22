package com.example.ambulanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private UserRepository userRepository;
    private EditText editTextName;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDatabase db = userDatabase.getInstance(getApplicationContext());
        userRepository = new UserRepository(db.userDao());

        editTextName = findViewById(R.id.NameEditText);
        editTextPassword = findViewById(R.id.PasswordEditText);
        editTextPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        Button registerButton = findViewById(R.id.LoginButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String name = editTextName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if name and password are not empty
                if (name.isEmpty() || password.isEmpty() || name.equalsIgnoreCase("admin")) {
                    Toast.makeText(RegisterActivity.this, "Invalid Name/Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new user
                Users user = new Users();
                user.name = name;
                user.password = password;

                // Use Executor to perform database operation in the background
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    // Register the user
                    userRepository.register(user);

                    // Update UI on the main thread
                    runOnUiThread(() -> {
                        // Show a success message
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    });
                });
            }
        });
    }
}
