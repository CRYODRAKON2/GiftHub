package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersTableDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "donation_app.db";
    private static final int DATABASE_VERSION = 1;

    public UsersTableDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "userid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "address TEXT" +
                ")");
    }

    // Insert Users
    public long insertUser(String name, String email, String password, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("address", address);
        long newRowId = db.insert("users", null, values);
        db.close();
        return newRowId;
    }

    // Check if user exists
    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { "userid" };
        String selection = "email = ?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);
        boolean userExists = (cursor.getCount() > 0);
        cursor.close();
        return userExists;
    }

    // authenticate the user
    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { "userid" };
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = { email, password };
        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);
        boolean userExists = (cursor.getCount() > 0);
        cursor.close();
        return userExists;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade strategy for database schema changes
    }
}
