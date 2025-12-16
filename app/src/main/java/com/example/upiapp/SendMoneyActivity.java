package com.example.upiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.upiapp.utils.LocalDataStore; // Import the utility class

public class SendMoneyActivity extends AppCompatActivity {

    private EditText editReceiverUpiId, editAmount, editMessage;
    private Button btnPay;
    private LocalDataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);

        dataStore = new LocalDataStore(this);

        // Initialize UI components
        editReceiverUpiId = findViewById(R.id.edit_receiver_upi_id);
        editAmount = findViewById(R.id.edit_amount);
        editMessage = findViewById(R.id.edit_message);
        btnPay = findViewById(R.id.btn_pay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePaymentFlow();
            }
        });
    }

    private void initiatePaymentFlow() {
        String receiverId = editReceiverUpiId.getText().toString().trim();
        String amountStr = editAmount.getText().toString().trim();
        String message = editMessage.getText().toString().trim();

        if (receiverId.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter receiver ID and amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format.", Toast.LENGTH_SHORT).show();
            return;
        }

        // *** CRITICAL CHANGE: Redirect to ConfirmPinActivity ***

        Toast.makeText(this, "Transaction initiated. Enter UPI PIN to confirm.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SendMoneyActivity.this, ConfirmPinActivity.class);

        // Pass all transaction data to the PIN confirmation screen
        intent.putExtra("RECEIVER_ID", receiverId);
        intent.putExtra("AMOUNT", amount);
        intent.putExtra("MESSAGE", message);
        intent.putExtra("IS_DEV_MODE", false); // Normal Mode flag

        startActivity(intent);
        finish(); // Finish current activity
    }
}