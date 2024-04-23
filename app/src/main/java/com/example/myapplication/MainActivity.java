package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening_page1);

        TextView textView4 = findViewById(R.id.textView4);

        textView4.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OpeningPageActivity2.class);
            startActivity(intent);
        });
    }
}