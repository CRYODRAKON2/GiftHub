package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClothesTableDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "donation_app.db";
    private static final int DATABASE_VERSION = 1;

    public ClothesTableDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE clothes (" +
                "clothid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "size TEXT," +
                "gender TEXT," +
                "age_group TEXT," +
                "credit_value INTEGER NOT NULL," +
                "defect TEXT," +
                "image BLOB" +
                ")");
    }

    //Insert Clothes
    public long insertClothing(String size, String gender, String ageGroup, int creditValue, String defect, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("size", size);
        values.put("gender", gender);
        values.put("age_group", ageGroup);
        values.put("credit_value", creditValue);
        values.put("defect", defect);
        values.put("image", image);
        long newRowId = db.insert("clothes", null, values);
        db.close();
        return newRowId;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade strategy for database schema changes
    }
}
