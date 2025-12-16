package com.example.upiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.upiapp.models.Transaction;
import com.example.upiapp.utils.LocalDataStore;

public class ConfirmPinActivity extends AppCompatActivity {

    private EditText editUpiPin;
    private Button btnConfirmPayment;
    private TextView textPaymentSummary;
    private LocalDataStore dataStore;

    // Variables to hold incoming transaction details
    private String receiverId;
    private double amount;
    private String message;
    private boolean isDeveloperMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pin);

        dataStore = new LocalDataStore(this);

        editUpiPin = findViewById(R.id.edit_upi_pin);
        btnConfirmPayment = findViewById(R.id.btn_confirm_payment);
        textPaymentSummary = findViewById(R.id.text_payment_summary);

        // 1. Get Transaction Details from the Intent
        Intent intent = getIntent();
        receiverId = intent.getStringExtra("RECEIVER_ID");
        amount = intent.getDoubleExtra("AMOUNT", 0.0);
        message = intent.getStringExtra("MESSAGE");
        isDeveloperMode = intent.getBooleanExtra("IS_DEV_MODE", false);

        // Display Summary
        String summary = String.format("Paying ₹%.2f to %s", amount, receiverId);
        textPaymentSummary.setText(summary);

        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateFinalTransaction();
            }
        });
    }

    private void initiateFinalTransaction() {
        String inputPin = editUpiPin.getText().toString().trim();

        if (inputPin.length() != 4) {
            Toast.makeText(this, "Please enter the 4-digit UPI PIN.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Validate PIN (against saved PIN from SetPinActivity)
        String savedPin = dataStore.getSavedPin();

        if (savedPin == null || !inputPin.equals(savedPin)) {
            Toast.makeText(this, "Incorrect UPI PIN.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. PIN is correct: Initiate the Fraud Check
        // In a real app, the server handles this. Here, we call the local simulator.
        Transaction resultTransaction = dataStore.simulateFraudCheck(receiverId, amount, message);

        // 3. Save to History
        dataStore.saveTransaction(resultTransaction);

        // 4. Redirect to the Result Screen
        Intent resultIntent = new Intent(ConfirmPinActivity.this, ResultActivity.class);
        resultIntent.putExtra("TRANSACTION_STATUS", resultTransaction.getStatus());
        resultIntent.putExtra("TRANSACTION_RISK", String.valueOf(resultTransaction.getRiskScore()));
        resultIntent.putExtra("TRANSACTION_REASON", resultTransaction.getReason());
        startActivity(resultIntent);

        finish();
    }
}