package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // get userid
        Intent previousIntent = getIntent();
        email = previousIntent.getStringExtra("email");

        // Donate Clothes
        LinearLayout clothDonation = findViewById(R.id.donateClothes);
        clothDonation.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, DonateCloth.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Donate Books
        LinearLayout bookDonation = findViewById(R.id.donateBooks);
        bookDonation.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, DonateBook.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Donate Clothes
        LinearLayout leaderboard = findViewById(R.id.LeaderBoard);
        leaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, Leaderboard.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        // Donate Clothes
        LinearLayout accountPage = findViewById(R.id.Account);
        accountPage.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, AccountPage.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });
    }
}