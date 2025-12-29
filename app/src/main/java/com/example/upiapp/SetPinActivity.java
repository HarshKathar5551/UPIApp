package com.example.upiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upiapp.models.SetPinRequest;
import com.example.upiapp.service.ApiService;
import com.example.upiapp.utils.LocalDataStore;

public class SetPinActivity extends AppCompatActivity {

    private EditText editNewPin;
    private EditText editConfirmPin;
    private Button btnSetPin;
    private LocalDataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);

        dataStore = new LocalDataStore(this);

        editNewPin = findViewById(R.id.edit_new_pin);
        editConfirmPin = findViewById(R.id.edit_confirm_pin);
        btnSetPin = findViewById(R.id.btn_set_pin);

        btnSetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSetPin();
            }
        });
    }

    private void attemptSetPin() {
        String password = editNewPin.getText().toString().trim();
        String pin = editConfirmPin.getText().toString().trim();

        if (pin.length() != 4) {
            Toast.makeText(this, "PIN must be exactly 4 digits.", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (!password.equals(pin)) {
//            Toast.makeText(this, "PINs do not match. Please re-enter.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        String token = dataStore.getAuthToken();
        if (token == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        SetPinRequest request = new SetPinRequest(password, pin);

        apiService.setPin("Bearer " + token, request)
                .enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(retrofit2.Call<Void> call,
                                           retrofit2.Response<Void> response) {

                        if (response.isSuccessful()) {

                            Toast.makeText(SetPinActivity.this,
                                    "UPI PIN set successfully",
                                    Toast.LENGTH_SHORT).show();

                            // Redirect to Main/Home screen
//                            startActivity(new Intent(SetPinActivity.this, MainActivity.class));
//                            finish();
                            // 2. Redirect to the Main Fragment Activity
                            Intent intent = new Intent(SetPinActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Close SetPinActivity

                        } else {
                            Toast.makeText(SetPinActivity.this,
                                    "Failed to set PIN. Wrong password?",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                        Toast.makeText(SetPinActivity.this,
                                "Network error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
//
//        // 1. Save the new PIN to SharedPreferences
//        dataStore.savePin(newPin); // Requires a new method in LocalDataStore
//
//        Toast.makeText(this, "PIN set successfully!", Toast.LENGTH_SHORT).show();
//
//        // 2. Redirect to the Main Fragment Activity
//        Intent intent = new Intent(SetPinActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish(); // Close SetPinActivity
    }
}