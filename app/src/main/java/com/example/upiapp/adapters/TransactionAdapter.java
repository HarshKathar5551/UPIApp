package com.example.upiapp.adapters;



import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.upiapp.R;
import com.example.upiapp.models.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        holder.textReceiver.setText(transaction.getReceiverId());
        holder.textAmount.setText("₹ " + String.format("%.2f", transaction.getAmount()));
        holder.textTimestamp.setText(transaction.getTimestamp());
        holder.textStatus.setText(transaction.getStatus() + " (Risk: " + transaction.getRiskScore() + ")");

        // Set status color based on decision
        String status = transaction.getStatus();
        if (status.equals("APPROVED")) {
            holder.textStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
        } else if (status.equals("FLAGGED")) {
            holder.textStatus.setTextColor(Color.parseColor("#FF9800")); // Orange/Yellow
        } else if (status.equals("BLOCKED")) {
            holder.textStatus.setTextColor(Color.RED); // Red
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView textReceiver, textAmount, textStatus, textTimestamp;

        TransactionViewHolder(View itemView) {
            super(itemView);
            textReceiver = itemView.findViewById(R.id.text_receiver);
            textAmount = itemView.findViewById(R.id.text_amount);
            textStatus = itemView.findViewById(R.id.text_status);
            textTimestamp = itemView.findViewById(R.id.text_timestamp);
        }
    }
}