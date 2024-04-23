package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CategoriesTableDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "donation_app.db";
    private static final int DATABASE_VERSION = 1;

    public CategoriesTableDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE categories (" +
                "genreid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "genre_name TEXT NOT NULL UNIQUE" +
                ")");
    }

    //Insert Genre
    public long insertCategory(String genreName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("genre_name", genreName);
        long newRowId = db.insert("categories", null, values);
        db.close();
        return newRowId;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade strategy for database schema changes
    }
}
