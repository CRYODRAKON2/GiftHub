package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "donation_app.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_USERS = "CREATE TABLE users (" +
            "userid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "email TEXT NOT NULL UNIQUE," +
            "password TEXT NOT NULL," +
            "address TEXT" +
            ")";

    private static final String CREATE_TABLE_CLOTHES = "CREATE TABLE clothes (" +
            "clothid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "size TEXT," +
            "gender TEXT," +
            "age_group TEXT," +
            "credit_value INTEGER NOT NULL," +
            "defect TEXT," +
            "image BLOB" +
            ")";

    private static final String CREATE_TABLE_DONATIONS = "CREATE TABLE donations (" +
            "donationid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "userid INTEGER NOT NULL," +
            "itemdonated TEXT NOT NULL," +
            "donation_date DATETIME NOT NULL," +
            "status TEXT NOT NULL," +
            "FOREIGN KEY (userid) REFERENCES users(userid)," +
            "FOREIGN KEY (itemdonated) REFERENCES books(bookid) ON DELETE CASCADE," +
            "FOREIGN KEY (itemdonated) REFERENCES clothes(clothid) ON DELETE CASCADE" +
            ")";




    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE categories (" +
            "genreid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "genre_name TEXT NOT NULL UNIQUE" +
            ")";

    private static final String CREATE_TABLE_BOOKS = "CREATE TABLE books (" +
            "bookid INTEGER PRIMARY KEY AUTOINCREMENT," +
            "bookname TEXT NOT NULL," +
            "author TEXT," +
            "genreid INTEGER," +
            "credit_value INTEGER NOT NULL," +
            "defect TEXT," +
            "FOREIGN KEY (genreid) REFERENCES categories(genreid)" +
            ")";
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // users table
        db.execSQL(CREATE_TABLE_USERS);
        // clothes table
        db.execSQL(CREATE_TABLE_CLOTHES);
        // donations table
        db.execSQL(CREATE_TABLE_DONATIONS);
        // categories table
        db.execSQL(CREATE_TABLE_CATEGORIES);
        // books table
        db.execSQL(CREATE_TABLE_BOOKS);
    }

    // -------------------------------------------------------------Users Table-----------------------------------------------------
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

    // get userid
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Default value if user not found
        String[] columns = {"userid"};
        String selection = "email=?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("userid");
            if (columnIndex != -1) {
                userId = cursor.getInt(columnIndex);
            }
            cursor.close();
        }
        db.close();

        return userId;
    }


    // get email by id
    public String getEmailById() {
        SQLiteDatabase db = this.getReadableDatabase();
        String email = "";
        String[] columns = {"email"};
        String selection = "userid=?";
        String[] selectionArgs = {"1"};

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("email");
            if (columnIndex != -1) {
                email = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        db.close();

        return "user@gmail.com";
    }


    // get user name
    public String getUserNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = "-1"; // Default value if user not found
        String[] columns = {"name"};
        String selection = "email=?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("name");
            if (columnIndex != -1) {
                name = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        db.close();

        return name;
    }


    // Update profile on userid
    public boolean updateUser(int userId, String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);

        String selection = "userid = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        int rowsUpdated = db.update("users", values, selection, selectionArgs);
        if (rowsUpdated > 0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }




    // --------------------------------------------------------------Clothes Table -------------------------------------------------------------------
    //Insert Clothes
    public long insertClothing(String size, String gender, String ageGroup, int creditValue, String defect, Bitmap image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("size", size);
        values.put("gender", gender);
        values.put("age_group", ageGroup);
        values.put("credit_value", creditValue);
        values.put("defect", defect);
        values.put("image", image.toString());
        long newRowId = db.insert("clothes", null, values);
        db.close();
        return newRowId;
    }

    // get the total credits per user
    public int getTotalClothCreditsAwardedTOUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalCredits = 0;

        // Query to calculate the sum of credits awarded by the user
        String query = "SELECT SUM(credit_value) AS total_credits FROM clothes WHERE clothid IN " +
                "(SELECT itemdonated FROM donations WHERE userid = ?)";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            int totalCreditsIndex = cursor.getColumnIndex("total_credits");
            if (totalCreditsIndex != -1) {
                totalCredits = cursor.getInt(totalCreditsIndex);
            } else {
                // Handle the case where the column index is not found
                Log.e("DbHelper", "Column index for total_credits not found");
            }
        }

        cursor.close();
        db.close();
        return totalCredits;
    }




    //-------------------------------------------------------------------Donations Table-------------------------------------
    //Insert Donations
    public long insertDonation(int userId, int itemDonated, String donationDate, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userid", userId);
        String donatedItem;
        if(itemDonated==0){donatedItem = "Book"; } else{donatedItem = "CLoth";}
        values.put("itemdonated", donatedItem); //cloth is 1 and book is 0
        values.put("donation_date", donationDate);
        values.put("status", status);
        long newRowId = db.insert("donations", null, values);
        db.close();
        return newRowId;
    }


    //-------------------------------------------------------------------Categories Table-----------------------------------
    //Insert Genre
    public long insertCategory(String genreName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("genre_name", genreName);
        long newRowId = db.insert("categories", null, values);
        db.close();
        return newRowId;
    }

    //Get genreId after inserting genre or just get genreId
    public int getOrInsertGenreId(String genreName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int genreId = -1; // Default value if genre does not exist

        // First, check if the genre exists
        Cursor cursor = db.rawQuery("SELECT genreid FROM categories WHERE genre_name = ?", new String[]{genreName});
        int genreIdColumnIndex = cursor.getColumnIndex("genreid");
        if (cursor.moveToFirst() && genreIdColumnIndex != -1) {
            // Genre exists, retrieve its genreId
            genreId = cursor.getInt(genreIdColumnIndex);
        } else {
            // Genre does not exist, insert it and retrieve its genreId
            ContentValues values = new ContentValues();
            values.put("genre_name", genreName);
            long newRowId = db.insert("categories", null, values);
            if (newRowId != -1) {
                // Genre inserted successfully, retrieve its genreId
                Cursor newCursor = db.rawQuery("SELECT genreid FROM categories WHERE rowid = ?", new String[]{String.valueOf(newRowId)});
                int newGenreIdColumnIndex = newCursor.getColumnIndex("genreid");
                if (newCursor.moveToFirst() && newGenreIdColumnIndex != -1) {
                    genreId = newCursor.getInt(newGenreIdColumnIndex);
                }
                newCursor.close();
            }
        }

        cursor.close();
        db.close();
        return genreId;
    }




    //-------------------------------------------------------------------Books Table-------------------------------------------------------
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

    // get the total credits per user
    public int getTotalBookCreditsAwardedToUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int totalCredits = 0;

        // Query to calculate the sum of credits awarded by the user
        String query = "SELECT SUM(credit_value) AS total_credits FROM books WHERE genreid IN " +
                "(SELECT genreid FROM categories WHERE genreid IN " +
                "(SELECT itemdonated FROM donations WHERE userid = ?))";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            int totalCreditsIndex = cursor.getColumnIndex("total_credits");
            if (totalCreditsIndex != -1) {
                totalCredits = cursor.getInt(totalCreditsIndex);
            } else {
                // Handle the case where the column index is not found
                Log.e("DbHelper", "Column index for total_credits not found");
            }
        }

        cursor.close();
        db.close();
        return totalCredits;
    }




    // ------------------------------------------------------------------Default methods---------------------------------------------
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS clothes");
        db.execSQL("DROP TABLE IF EXISTS donations");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS books");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS clothes");
        db.execSQL("DROP TABLE IF EXISTS donations");
        db.execSQL("DROP TABLE IF EXISTS categories");
        db.execSQL("DROP TABLE IF EXISTS books");
        onCreate(db);
    }
}
