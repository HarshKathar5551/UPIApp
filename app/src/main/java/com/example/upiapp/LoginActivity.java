package com.example.upiapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upiapp.models.LoginRequest;
import com.example.upiapp.models.LoginResponse;
import com.example.upiapp.service.ApiService;
import com.example.upiapp.utils.LocalDataStore;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPassword;
    private Button btnLogin;
    private TextView textSignupInstead;
    private LocalDataStore dataStore;
    private final String DUMMY_OTP = "1236";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dataStore = new LocalDataStore(this);

        // CHECK LOGIN STATUS FIRST (Crucial for the overall flow)
        if (dataStore.isLoggedIn()) {
            // NOTE: If you decide to add PIN validation on startup, this needs to check for a PIN too.
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        // Initialize UI components
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
        textSignupInstead = findViewById(R.id.text_signup_instead);

        // Dummy Logic: Enable login button only if fields are not empty
        TextWatcher loginTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enableLogin = !editUsername.getText().toString().trim().isEmpty() &&
                        !editPassword.getText().toString().trim().isEmpty();
                btnLogin.setEnabled(enableLogin);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        editUsername.addTextChangedListener(loginTextWatcher);
        editPassword.addTextChangedListener(loginTextWatcher);

        // Initially disable the login button
        btnLogin.setEnabled(false);

        // Handle Login button click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        // Handle the "Sign Up instead" link click
        textSignupInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirects to the new SignupActivity
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void performLogin() {

        String upiId = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (upiId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter UPI ID and Password", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        LoginRequest request = new LoginRequest(upiId, password);

        apiService.login(request).enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(retrofit2.Call<LoginResponse> call,
                                   retrofit2.Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse loginResponse = response.body();

                    // ✅ Save JWT token
                    dataStore.saveAuthToken(loginResponse.getToken());
                    Log.d("LOGIN", "Token: " + loginResponse.getToken());

                    // ✅ Save user details)

                    // ✅ Mark user logged in
                    dataStore.setLoggedIn(true);

                    Toast.makeText(LoginActivity.this,
                            "Login successful! Welcome " + loginResponse.getUser().getName(),
                            Toast.LENGTH_SHORT).show();

                    // Redirect to Set PIN / Main flow
                    Intent intent = new Intent(LoginActivity.this, SetPinActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Invalid credentials" + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}