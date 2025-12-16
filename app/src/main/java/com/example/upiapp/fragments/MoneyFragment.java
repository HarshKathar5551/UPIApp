package com.example.upiapp.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // Added for the balance TextView
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.upiapp.R;
import com.example.upiapp.adapters.TransactionAdapter;
import com.example.upiapp.models.Transaction;
import com.example.upiapp.utils.LocalDataStore;

public class MoneyFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textAccountBalance; // NEW: Declare the balance TextView
    private TextView textEmptyHistory;   // NEW: For handling empty history UI
    private LocalDataStore dataStore;

    // Dummy Balance for the demo
    private static final String DUMMY_BALANCE = "₹ 50,000.00";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_money, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataStore = new LocalDataStore(getActivity());

        // Initialize UI components
        recyclerView = view.findViewById(R.id.recycler_view_history);
        textAccountBalance = view.findViewById(R.id.text_account_balance); // NEW: Initialize balance TextView
        textEmptyHistory = view.findViewById(R.id.text_empty_history);       // NEW: Initialize empty history TextView

        // Set the dummy balance
        textAccountBalance.setText(DUMMY_BALANCE);

        // Set up RecyclerView layout manager once
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // The list will be refreshed in onResume()
    }

    // Load history data every time the fragment is visible
    @Override
    public void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        List<Transaction> transactions = dataStore.getTransactions();

        if (transactions.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            // Show the empty history message
            if (textEmptyHistory != null) {
                textEmptyHistory.setVisibility(View.VISIBLE);
                // Removed the redundant Toast here, as the TextView shows the message
            }
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            // Hide the empty history message
            if (textEmptyHistory != null) {
                textEmptyHistory.setVisibility(View.GONE);
            }

            // Always set a new adapter (or update, setting new is simpler for demo)
            TransactionAdapter adapter = new TransactionAdapter(transactions);
            recyclerView.setAdapter(adapter);
        }
    }
}