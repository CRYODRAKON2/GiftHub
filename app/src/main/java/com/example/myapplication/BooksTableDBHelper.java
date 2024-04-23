package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BooksTableDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "donation_app.db";
    private static final int DATABASE_VERSION = 1;

    public BooksTableDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE books (" +
                "bookid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bookname TEXT NOT NULL," +
                "author TEXT," +
                "genreid INTEGER," +
                "credit_value INTEGER NOT NULL," +
                "defect TEXT," +
                "FOREIGN KEY (genreid) REFERENCES categories(genreid)" +
                ")");
    }

    //Insert Books
    public long insertBook(String bookname, String author, int genreid, int creditValue, String defect) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bookname", bookname);
        values.put("author", author);
        values.put("genreid", genreid);
        values.put("credit_value", creditValue);
        values.put("defect", defect);
        long newRowId = db.insert("books", null, values);
        db.close();
        return newRowId;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade strategy for database schema changes
    }
}
