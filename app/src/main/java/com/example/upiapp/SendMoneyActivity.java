package com.example.upiapp;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.upiapp.models.Transaction;
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

        // 1. Show Processing Screen (Simulated: No actual separate screen, just a delay/loader is needed) [cite: 93, 94, 95]
        Toast.makeText(this, "Verifying transaction with Fraud Engine...", Toast.LENGTH_LONG).show();

        // 2. Simulate Fraud Detection Check (Backend call substitute)
        // **This is the point where your friend's API call will eventually replace the line below.**
        Transaction resultTransaction = dataStore.simulateFraudCheck(receiverId, amount, message);

        // 3. Store Transaction (locally for history demo) [cite: 63]
        dataStore.saveTransaction(resultTransaction);

        // 4. Navigate to Result Screen [cite: 96]
        Intent intent = new Intent(SendMoneyActivity.this, ResultActivity.class);
        intent.putExtra("STATUS", resultTransaction.getStatus());
        intent.putExtra("REASON", resultTransaction.getReason());
        intent.putExtra("TXN_ID", resultTransaction.getTransactionId());
        intent.putExtra("RISK_SCORE", resultTransaction.getRiskScore());
        startActivity(intent);
        finish(); // Finish current activity to prevent going back to it
    }
}
