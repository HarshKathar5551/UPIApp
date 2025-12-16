package com.example.upiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import androidx.core.content.FileProvider;

import androidx.appcompat.app.AppCompatActivity;
import com.example.upiapp.utils.LocalDataStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceiveMoneyActivity extends AppCompatActivity {

    private LocalDataStore dataStore;
    private TextView textReceiveUpiId;
    private ImageView imageQrCode;
    private Button btnShareUpiId;
    private String userUpiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_money);

        dataStore = new LocalDataStore(this);
        textReceiveUpiId = findViewById(R.id.text_receive_upi_id);
        imageQrCode = findViewById(R.id.image_qr_code);
        btnShareUpiId = findViewById(R.id.btn_share_upi_id);

        displayUserUpiId();
        setQrCode();

        btnShareUpiId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUpiIdAndQr();
            }
        });
    }

    private void displayUserUpiId() {
        String mobileNumber = dataStore.getSavedUsername();

        if (mobileNumber != null) {
            userUpiId = mobileNumber + "@demoupi";
            textReceiveUpiId.setText(userUpiId);
        } else {
            userUpiId = "user_not_found@demoupi";
            textReceiveUpiId.setText("Error: User ID not found");
            Toast.makeText(this, "User data error. Please re-login.", Toast.LENGTH_LONG).show();
        }
    }

    private void setQrCode() {
        // ... (setQrCode remains the same - placeholder logic) ...
    }


    private void shareUpiIdAndQr() {
        if (userUpiId == null || userUpiId.isEmpty()) {
            Toast.makeText(this, "UPI ID is not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri imageUri = getUriForDrawable();

        if (imageUri != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            // NOTE: Keep type as image/png or image/* to ensure the image is included.
            // Some apps may ignore EXTRA_TEXT when EXTRA_STREAM is present.
            shareIntent.setType("image/png");

            // Attach the image URI
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            // Attach the text message (This relies on the receiving app's support)
            shareIntent.putExtra(Intent.EXTRA_TEXT, "My UPI ID for payments is: " + userUpiId + ". Scan my QR to pay!");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My UPI ID & QR Code");

            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Share UPI Details via"));
        } else {
            Toast.makeText(this, "Could not prepare QR code image for sharing.", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getUriForDrawable() {
        try {
            // Get the bitmap from the ImageView
            BitmapDrawable drawable = (BitmapDrawable) imageQrCode.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            File file = new File(cachePath, "qr_code_share.png");

            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            // Use FileProvider to get a shareable URI (Ensure this authority matches your Manifest)
            return FileProvider.getUriForFile(this, "com.example.upiapp.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}