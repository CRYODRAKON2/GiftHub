package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OpeningPageActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_page2);

        // initializing components
        TextView next = findViewById(R.id.textView4);

        // initializing component actions
        next.setOnClickListener(v -> {
            Intent intent = new Intent(OpeningPageActivity2.this, OpeningPageActivity3.class);
            startActivity(intent);
        });
    }
}
