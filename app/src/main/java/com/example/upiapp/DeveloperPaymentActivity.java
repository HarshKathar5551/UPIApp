package com.example.upiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.upiapp.utils.LocalDataStore;

public class DeveloperPaymentActivity extends AppCompatActivity {

    private EditText editReceiverId, editAmount, editMessage;
    private Button btnInitiatePayment;
    private LocalDataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_payment);

        dataStore = new LocalDataStore(this);

        editReceiverId = findViewById(R.id.edit_dev_receiver_id);
        editAmount = findViewById(R.id.edit_dev_amount);
        editMessage = findViewById(R.id.edit_dev_message);
        btnInitiatePayment = findViewById(R.id.btn_dev_initiate_payment);

        btnInitiatePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePaymentFlow();
            }
        });
    }

    private void initiatePaymentFlow() {
        String receiverId = editReceiverId.getText().toString().trim();
        String amountStr = editAmount.getText().toString().trim();
        String message = editMessage.getText().toString().trim();

        if (receiverId.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter Receiver ID and Amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);

            // *** CRITICAL CHANGE: Redirect to ConfirmPinActivity ***

            Toast.makeText(this, "Dev Transaction initiated. Enter UPI PIN to confirm.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(DeveloperPaymentActivity.this, ConfirmPinActivity.class);

            // Pass test transaction data
            intent.putExtra("RECEIVER_ID", receiverId);
            intent.putExtra("AMOUNT", amount);
            intent.putExtra("MESSAGE", message);
            intent.putExtra("IS_DEV_MODE", true); // Developer Mode flag

            startActivity(intent);
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount entered.", Toast.LENGTH_SHORT).show();
        }
    }
}