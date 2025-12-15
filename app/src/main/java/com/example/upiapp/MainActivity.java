package com.example.upiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        Button btnSendMoney = findViewById(R.id.btn_send_money);
        Button btnHistory = findViewById(R.id.btn_history);
        Button btnProfile = findViewById(R.id.btn_profile);

        // Navigation for Send Money Screen
        btnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SendMoneyActivity.class));
            }
        });

        // Navigation for Transaction History Screen
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        // Navigation for Profile Screen
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We haven't created this activity yet, but we define the navigation
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
    }
}