package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    // Variable Declarations
    private static final String EMAIL_REGEX =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private static final int REQUEST_CODE_MAPS = 100;
    EditText nameEt, emailEt, passwordEt;
    TextView addressEt;
    TextView toLoginPage, signUp;

    // Instance Creations
    DbHelper usersDbHelper = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        // initializing components
        nameEt = findViewById(R.id.editTextName);
        emailEt = findViewById(R.id.editTextEmail);
        passwordEt = findViewById(R.id.editTextPassword);
        addressEt = findViewById(R.id.editTextAddress);
        toLoginPage = (TextView) findViewById(R.id.textViewLogin);
        signUp = (TextView) findViewById(R.id.textView8);

        // initializing component actions
        toLoginPage.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });

        addressEt.setOnClickListener(view -> {
            Intent mapsIntent = new Intent(Register.this, GMaps.class);
            startActivityIfNeeded(mapsIntent, REQUEST_CODE_MAPS);
        });

        signUp.setOnClickListener(v -> {
            String name, email, password, address;
            name = nameEt.getText().toString();
            email = emailEt.getText().toString();
            password = passwordEt.getText().toString();
            Intent previousIntent = getIntent();
            address = previousIntent.getStringExtra("location");

            if(name.isEmpty() && email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(this, "Please enter all the details", Toast.LENGTH_LONG).show();
                    return;
            }

            if(!isValidEmail(email)) {
                Toast.makeText(this, "Please enter valid email", Toast.LENGTH_LONG).show();
                emailEt.setText("");
                return;
            }

            boolean userExists = usersDbHelper.checkUserExists(email);
            if(!userExists) {
                // add user only if user does not exist
                System.out.println("Users Does not exists: " + name + email + password + address);
                long newRowId = usersDbHelper.insertUser(name, email, password, address);
                if (newRowId != -1) {
                    System.out.println("Registration Done: " + name + email + password + address);
                    Toast.makeText(this, "Congratulations on successful registration", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                } else {
                    System.out.println("Registration Failed: " + name + email + password + address);
                    Toast.makeText(this, "Please retry registering, an error has occured", Toast.LENGTH_LONG).show();
                }
            } else {
                System.out.println("Users exists: " + name + email + password + address);
                Toast.makeText(this, "Proceed to login, User with email already exists", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MAPS && resultCode == RESULT_OK && data != null) {
            String address = data.getStringExtra("location");
            if (address != null) {
                addressEt.setText(address);
            }
        }
    }

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
