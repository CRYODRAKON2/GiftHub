package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    // Variable declarations
    TextView logInTv;
    EditText emailEt, passwordEt;

    // Instance Creations
    DbHelper usersDbHelper = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // initializing components
        logInTv = findViewById(R.id.textView8);
        emailEt = findViewById(R.id.editTextName);
        passwordEt = findViewById(R.id.editTextPassword);

        // initializing component actions
        logInTv.setOnClickListener(v -> {
            String email, password;
            email = emailEt.getText().toString();
            password = passwordEt.getText().toString();

            if(email.isEmpty() && password.isEmpty()){
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_LONG).show();
                return;
            }

            boolean userExists = usersDbHelper.checkUserExists(email);
            if(userExists) {
                // add user only if user does not exist
                boolean loggedIn = usersDbHelper.loginUser(email, password);
                if (loggedIn) {
                    Toast.makeText(this, "Congratulations on successful Login", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, HomePage.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please retry registering", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Proceed to login, User with email already exists", Toast.LENGTH_LONG).show();
            }
        });
    }
}
