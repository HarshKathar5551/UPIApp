package com.example.upiapp.utils;


import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.upiapp.models.Transaction;

public class LocalDataStore {
    private static final String PREF_NAME = "UpiDemoPrefs";
    private static final String KEY_LOGGED_IN = "isLoggedIn";
    private static final String KEY_TRANSACTIONS = "transactionsList";
    private SharedPreferences sharedPrefs;
    private Gson gson;

    public LocalDataStore(Context context) {
        sharedPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // --- Authentication ---
    public boolean isLoggedIn() {
        return sharedPrefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        sharedPrefs.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply();
    }

    public void logout() {
        sharedPrefs.edit().remove(KEY_LOGGED_IN).apply();
    }

    // --- Fraud Engine Simulation (No change to logic) ---
    public Transaction simulateFraudCheck(String receiverId, double amount, String message) {
        java.util.Random random = new java.util.Random();
        int decisionInt = random.nextInt(100);

        String status;
        int riskScore;
        String reason;

        if (decisionInt < 60) {
            status = "APPROVED";
            riskScore = random.nextInt(30);
            reason = "Standard transaction pattern.";
        } else if (decisionInt < 85) {
            status = "FLAGGED";
            riskScore = random.nextInt(30) + 50;
            reason = "Unusual amount/new receiver.";
        } else {
            status = "BLOCKED";
            riskScore = random.nextInt(20) + 80;
            reason = "High velocity transaction alert.";
        }

        return new Transaction(receiverId, amount, message, status, riskScore, reason);
    }

    // --- Local Transaction History (UPDATED) ---

    public void saveTransaction(Transaction transaction) {
        List<Transaction> transactions = getTransactions();
        transactions.add(0, transaction); // Add to the start so the newest is first

        String json = gson.toJson(transactions);
        sharedPrefs.edit().putString(KEY_TRANSACTIONS, json).apply();
    }

    public List<Transaction> getTransactions() {
        String json = sharedPrefs.getString(KEY_TRANSACTIONS, null);
        if (json == null) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        return gson.fromJson(json, type);
    }
}