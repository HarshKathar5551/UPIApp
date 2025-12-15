package com.example.upiapp;

package com.example.upiapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.upiapp.adapters.TransactionAdapter;
import com.example.upiapp.models.Transaction;
import com.example.upiapp.utils.LocalDataStore;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LocalDataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dataStore = new LocalDataStore(this);
        recyclerView = findViewById(R.id.recycler_view_history);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadHistory();
    }

    private void loadHistory() {
        // Get the list of transactions from our local data store
        List<Transaction> transactions = dataStore.getTransactions();

        // Initialize and set the Adapter
        TransactionAdapter adapter = new TransactionAdapter(transactions);
        recyclerView.setAdapter(adapter);
    }
}