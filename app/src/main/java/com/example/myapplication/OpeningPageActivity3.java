package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OpeningPageActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_page3);

        // initializing components
        TextView next = findViewById(R.id.textView7);

        // initializing component actions
        next.setOnClickListener(v -> {
            Intent intent = new Intent(OpeningPageActivity3.this, OpeningPageActivity4.class);
            startActivity(intent);
        });
    }
}
