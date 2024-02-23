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

    private EditText editTextName;
    private EditText editTextPassword;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextName = findViewById(R.id.NameEditText);
        editTextPassword = findViewById(R.id.PasswordEditText);
        editTextPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        userRepository = new UserRepository(userDatabase.getInstance(getApplicationContext()).userDao());

        Button loginButton = findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void login() {
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
                    // Navigate to the next activity (assuming DisplayUsersActivity.class is the name of your next activity)
                    Intent intent = new Intent(LoginActivity.this, DisplayUsersActivity.class);
                    startActivity(intent);
                } else if((name.equals("developer") || name.equals("dev")) && password.equals("1234")){

                    Intent intent = new Intent(LoginActivity.this, ManageDividersActivity.class);
                    startActivity(intent);


                }else if (user != null) {
                    // Login successful
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, SetPathActivity.class);
                    startActivity(intent);
                    // You can add code here to navigate to the main activity or perform other actions
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, "Login failed. Please retry.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
