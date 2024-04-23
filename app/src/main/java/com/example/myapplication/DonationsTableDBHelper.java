package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DonationsTableDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "donation_app.db";
    private static final int DATABASE_VERSION = 1;

    public DonationsTableDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE donations (" +
                "donationid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userid INTEGER NOT NULL," +
                "itemdonated INTEGER NOT NULL," +
                "donation_date DATETIME NOT NULL," +
                "status TEXT," +
                "FOREIGN KEY (userid) REFERENCES users(userid)," +
                "FOREIGN KEY (itemdonated) REFERENCES books(bookid) ON DELETE CASCADE," +
                "FOREIGN KEY (itemdonated) REFERENCES clothes(clothid) ON DELETE CASCADE" +
                ")");
    }

    //Insert Donations
    public long insertDonation(int userId, int itemDonated, String donationDate, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userid", userId);
        values.put("itemdonated", itemDonated);
        values.put("donation_date", donationDate);
        values.put("status", status);
        long newRowId = db.insert("donations", null, values);
        db.close();
        return newRowId;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade strategy for database schema changes
    }
}
