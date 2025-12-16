package com.example.upiapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.upiapp.LoginActivity;
import com.example.upiapp.R;
import com.example.upiapp.SetPinActivity;
import com.example.upiapp.SecretDeveloperActivity; // NEW IMPORT
import com.example.upiapp.utils.LocalDataStore;

public class ProfileFragment extends Fragment {

    private LocalDataStore dataStore;
    private Button btnLogout;
    private Button btnChangeUpiPin;

    // UI elements to display profile data
    private TextView textUsername, textPhoneNumber, textUpiId;

    // --- SECRET TAP LOGIC VARIABLES ---
    private static final int REQUIRED_TAPS = 7;
    private static final long RESET_TIME_MS = 1000; // 1 second window
    private int tapCount = 0;
    private long lastTapTime = 0;
    // --- END SECRET TAP LOGIC ---


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataStore = new LocalDataStore(getActivity());

        // Initialize UI components
        textUsername = view.findViewById(R.id.text_username);
        // ... (other initialization code remains the same) ...
        textPhoneNumber = view.findViewById(R.id.text_phone_number);
        textUpiId = view.findViewById(R.id.text_upi_id);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnChangeUpiPin = view.findViewById(R.id.btn_change_upi_pin);

        // Assuming your profile layout has a TextView for the username with ID: R.id.text_username
        // We set the secret tap listener on the username field.
        textUsername.setOnClickListener(v -> handleSecretTap());

        // ... (rest of the listeners and displayProfileData call remain the same) ...

        // Listener for Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });

        // Listener for Change UPI PIN
        btnChangeUpiPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetPinActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "Redirecting to Change UPI PIN screen.", Toast.LENGTH_SHORT).show();
            }
        });

        displayProfileData(); // Ensure this is still called
    }

    /**
     * Handles the logic for the secret consecutive taps.
     */
    private void handleSecretTap() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastTapTime < RESET_TIME_MS) {
            // Tap is within the required time window
            tapCount++;

            if (tapCount == REQUIRED_TAPS) {
                // SUCCESS: Open the secret page
                Toast.makeText(getActivity(), "Developer mode unlocked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SecretDeveloperActivity.class);
                startActivity(intent);

                // Reset counter after success
                tapCount = 0;
            } else if (tapCount > REQUIRED_TAPS - 3) {
                // Give a hint when the user is close (e.g., last 3 taps)
                Toast.makeText(getActivity(), (REQUIRED_TAPS - tapCount) + " taps remaining...", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Tap time window expired, reset the counter
            tapCount = 1;
        }

        lastTapTime = currentTime;
    }


    private void displayProfileData() {
        // ... (displayProfileData logic remains the same) ...
        String mobileNumber = dataStore.getSavedUsername();
        String displayName = dataStore.getSavedDisplayName();

        String displayMobile = mobileNumber != null ? "+91 " + mobileNumber : "N/A";
        String displayUpiId = mobileNumber != null ? mobileNumber + "@demoupi" : "N/A";

        if (mobileNumber != null) {
            textUsername.setText(displayName);
            textPhoneNumber.setText(displayMobile);
            textUpiId.setText(displayUpiId);
        } else {
            textUsername.setText("Guest User");
            textPhoneNumber.setText("N/A");
            textUpiId.setText("N/A");
        }
    }

    private void performLogout() {
        // ... (performLogout logic remains the same) ...
        dataStore.logout();

        Toast.makeText(getActivity(), "Logged out successfully.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}