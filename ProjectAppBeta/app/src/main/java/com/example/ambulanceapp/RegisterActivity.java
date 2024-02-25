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

    // user table repository declaration
    private UserRepository userRepository;

    // required fields declared
    private EditText editTextName,editTextPassword;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //instance of database created
        userDatabase db = userDatabase.getInstance(getApplicationContext());
        userRepository = new UserRepository(db.userDao());

        //connecting xml fields
        editTextName = findViewById(R.id.NameEditText);
        editTextPassword = findViewById(R.id.PasswordEditText);
        registerButton = findViewById(R.id.LoginButton);

        //to hide the password field
        editTextPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // after clicking on register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                // Retrieve user input
                String name = editTextName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if name and password are not empty , admin , developer
                if (name.isEmpty() || password.isEmpty() || name.equalsIgnoreCase("admin") || name.equalsIgnoreCase("developer")) {
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

                        // direct user to login page
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    });
                });
            }
        });
    }
}
