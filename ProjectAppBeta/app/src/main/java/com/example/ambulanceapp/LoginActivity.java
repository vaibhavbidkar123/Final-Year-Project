package com.example.ambulanceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    //required fields
    private EditText editTextName,editTextPassword;
    private Button loginButton;

    //user table repository declaration
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //creating new user repository
        userRepository = new UserRepository(userDatabase.getInstance(getApplicationContext()).userDao());

        //xml connections
        editTextName = findViewById(R.id.NameEditText);
        editTextPassword = findViewById(R.id.PasswordEditText);
        loginButton = findViewById(R.id.LoginButton);

        //hide password field
        editTextPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //after clicking on login button , if user is driver direct to set route , admin then direct to users db , developer then to manage dividers page
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Retrieve user input
                String name = editTextName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Use Executor to perform database operation in the background
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    // Retrieve the user from the database based on the entered credentials
                    Users user = userRepository.login(name, password);

                    // Update UI on the main thread
                    runOnUiThread(() -> {
                        // Check if login was successful
                        if ((name.equals("admin") || name.equals("Admin")) && password.equals("1234")) {
                            // Navigate to the next activity ( DisplayUsersActivity.class)
                            Intent intent = new Intent(LoginActivity.this, DisplayUsersActivity.class);
                            startActivity(intent);
                        } else if((name.equals("developer") || name.equals("dev")) && password.equals("1234")){
                            // Navigate to the next activity ( AddShowDividersAcitivity.class)
                            Intent intent = new Intent(LoginActivity.this, AddShowDividersAcitivity.class);
                            startActivity(intent);

                        }else if (user != null) {
                            // Login successful
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, SetPathActivity.class);
                            startActivity(intent);
                        } else {
                            // Login failed
                            Toast.makeText(LoginActivity.this, "Login failed. Please retry.", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });
    }
}
