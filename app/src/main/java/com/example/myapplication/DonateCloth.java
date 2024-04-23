package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class DonateCloth extends AppCompatActivity {

    // Variable Declaration
    private static final int REQUEST_SELECT_IMAGE = 100;
    EditText clothAgeGroupEt, clothSizeEt, genderEt;
    Spinner spinner;
    ImageView addClothesIv;
    Integer userid;
    String email;
    Bitmap bitmap;

    // DBHelper declaration
    DbHelper clothesDbHelper = new DbHelper(this);
    DbHelper usersDbHelper = new DbHelper(this);
    DbHelper donationDbHelper = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloth_donation_form);

        // variable initialization
        clothAgeGroupEt = findViewById(R.id.editTextClothAgeGroup);
        clothSizeEt = findViewById(R.id.editTextClothSize);
        genderEt = findViewById(R.id.editTextGender);
        spinner = findViewById(R.id.spinner);
        addClothesIv = findViewById(R.id.imageView6);

        // get email
        Intent previousIntent = getIntent();
        email = previousIntent.getStringExtra("email");
        Toast.makeText(this, "Email: " + email, Toast.LENGTH_SHORT).show();

        // set spinner values
        String[] values = {"No, I don't want to be", "Yes, please keep me autonomous"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Click listener for addClothesIv ImageView
        addClothesIv.setOnClickListener(v -> {
            //Toast.makeText(this, "Adding image is left", Toast.LENGTH_SHORT).show();
            openGallery();
        });

        // Button
        TextView donateClothes = findViewById(R.id.textView15);
        donateClothes.setOnClickListener(v -> {
            int creditValue;
            String clothAgeGroup, clothSize, gender, defect;
            clothAgeGroup = clothAgeGroupEt.getText().toString();
            clothSize = clothSizeEt.getText().toString();
            gender = genderEt.getText().toString();
            Bitmap image = bitmap; //edit this
            defect = "None"; // edit this
            creditValue = 10;

            if (gender.isEmpty() || clothSize.isEmpty() || clothAgeGroup.isEmpty()) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_LONG).show();
                return;
            }

            if (email == null || email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
                return;
            }

            long newRowIdCloth = clothesDbHelper.insertClothing(clothSize, gender, clothAgeGroup, creditValue, defect, image);
            if (newRowIdCloth != -1) {
                Toast.makeText(this, "Cloth added successfully", Toast.LENGTH_SHORT).show();
                userid = usersDbHelper.getUserIdByEmail(email);
                long newRowIdDonation = donationDbHelper.insertDonation(userid, 1, getCurrentDate(), "Pending");
                if (newRowIdDonation != -1) {
                    Toast.makeText(this, "Successful Donation", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DonateCloth.this, HomePage.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Donation not successful", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Cloth not added successfully", Toast.LENGTH_SHORT).show();
            }
        });

        // Back to home button
        ImageView homeButton = findViewById(R.id.imageView14);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomePage.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }

    public String getCurrentDate() {
        // Get current date and time
        Date currentDate = new Date();

        // Define the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());

        // Format the date according to the defined format
        return dateFormat.format(currentDate);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityIfNeeded(galleryIntent, REQUEST_SELECT_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData() != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                // Set the selected image to the ImageView
                addClothesIv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
