package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Leaderboard extends AppCompatActivity {

    // variable declaration
    TextView bookCreditsTv, clothCreditsTv, totalCreditsTv;
    String email;
    int userid;

    // Database Helper
    DbHelper usersDbHelper = new DbHelper(this);
    DbHelper booksDbHelper = new DbHelper(this);
    DbHelper clothesDbHelper = new DbHelper(this);


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        // get email
        Intent previousIntent = getIntent();
        email = previousIntent.getStringExtra("email");
        Toast.makeText(this, "Email: " + email, Toast.LENGTH_SHORT).show();

        // variable initialization
        bookCreditsTv = findViewById(R.id.textView27);
        clothCreditsTv = findViewById(R.id.textView28);
        totalCreditsTv = findViewById(R.id.textView32);

        // fetching and setting the credit values
        String strBookCredits, strClothCredits, strTotalCredits;
        strBookCredits = bookCreditsTv.getText().toString();
        strClothCredits = clothCreditsTv.getText().toString();
        strTotalCredits = totalCreditsTv.getText().toString();

        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
            return;
        }

        strBookCredits = "Credits earned from book donations: 10 credits";
        strClothCredits = "Credits earned from cloth donations: 10 credits";
        strTotalCredits = "Total credits earned: 20 credits";

        userid = usersDbHelper.getUserIdByEmail(email);
        int bookCredits = booksDbHelper.getTotalBookCreditsAwardedToUser(userid);
        int clothCredits = clothesDbHelper.getTotalClothCreditsAwardedTOUser(userid);
        int totalCredits = bookCredits + clothCredits;

        strBookCredits += bookCredits + " credits";
        strClothCredits += clothCredits + " credits";
        strTotalCredits += totalCredits + " credits";

//        bookCreditsTv.setText(strBookCredits);
//        clothCreditsTv.setText(strClothCredits);
//        totalCreditsTv.setText(strTotalCredits);

        // Back to home button
        ImageView homeButton = findViewById(R.id.imageView14);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomePage.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }
}
