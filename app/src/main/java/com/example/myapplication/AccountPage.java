package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountPage extends AppCompatActivity {

    // variable declaration
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    TextView nameTv, editProfileTv, totalCreditsTv, contactUsTv, rulesAndRegulationsTv;
    String email;
    int userid;

    // DBHelper declaration
    DbHelper clothesDbHelper = new DbHelper(this);
    DbHelper usersDbHelper = new DbHelper(this);
    DbHelper booksDbHelper = new DbHelper(this);


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_page);

        // get email
        Intent previousIntent = getIntent();
        email = previousIntent.getStringExtra("email");

        //variable initialization
        nameTv = findViewById(R.id.textView22);
        totalCreditsTv = findViewById(R.id.textView26);
        editProfileTv = findViewById(R.id.textView23);
        contactUsTv = findViewById(R.id.textView24);
        rulesAndRegulationsTv = findViewById(R.id.textView25);

        // fetching and setting the credit values
        String strTotalCredits;
        strTotalCredits = totalCreditsTv.getText().toString();
        if (email == null || email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_LONG).show();
            return;
        }
        String n = usersDbHelper.getUserNameByEmail(email);
        nameTv.setText(n);
        userid = usersDbHelper.getUserIdByEmail(email);
        int bookCredits = booksDbHelper.getTotalBookCreditsAwardedToUser(userid);
        int clothCredits = clothesDbHelper.getTotalClothCreditsAwardedTOUser(userid);
        int totalCredits = bookCredits + clothCredits;
        strTotalCredits += totalCredits + " credits";
        totalCreditsTv.setText(strTotalCredits);

        // edit profile alert builder and setter
        editProfileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an EditText for each input field
                final EditText nameEditText = new EditText(AccountPage.this);
                final EditText emailEditText = new EditText(AccountPage.this);
                final EditText passwordEditText = new EditText(AccountPage.this);

                // Set hints for the EditTexts
                nameEditText.setHint("Enter your name");
                emailEditText.setHint("Enter your email");
                passwordEditText.setHint("Enter your password");

                // Build the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountPage.this);
                builder.setTitle("Edit Profile");
                builder.setMessage("Please enter your new details:");
                builder.setView(nameEditText); // Add name EditText to dialog
                builder.setView(emailEditText); // Add email EditText to dialog
                builder.setView(passwordEditText); // Add password EditText to dialog
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle positive button click (Save)
                        String newName = nameEditText.getText().toString();
                        String newEmail = emailEditText.getText().toString();
                        String newPassword = passwordEditText.getText().toString();

                        // Perform actions with the entered data (e.g., update profile)
                        boolean updated = usersDbHelper.updateUser(userid, newName, newEmail, newPassword);
                        if(updated) {
                            Toast.makeText(AccountPage.this, "Updation Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AccountPage.this, "Retry updating", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle negative button click (Cancel)
                        dialog.dismiss(); // Dismiss the dialog
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        contactUsTv.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ContactUs"); // Set the title of the dialog
            builder.setMessage("How do you want to contact us?"); // Set the message to display

            // Add buttons to the dialog
            builder.setPositiveButton("Message", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Implement message functionality here
                    Intent messageIntent = new Intent(Intent.ACTION_SENDTO);
                    messageIntent.setData(Uri.parse("smsto:8867127625"));
                    startActivity(messageIntent);
                }
            });

            builder.setNegativeButton("Call", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Implement call functionality here
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:8867127625"));
                    startActivity(callIntent);
                }
            });

            // Create and show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        // Download Rules and Regulations
        rulesAndRegulationsTv.setOnClickListener(v -> {
            // Path to the PDF file in your app's assets folder
            String pdfPath = "./RaR";

            // Create an Intent with action view
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Set the data and type of the intent
            intent.setDataAndType(Uri.parse(pdfPath), "application/pdf");

            try {
                // Try to start the activity to view the PDF
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Catch the exception if no application can handle the PDF file
                Toast.makeText(this, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
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
}
