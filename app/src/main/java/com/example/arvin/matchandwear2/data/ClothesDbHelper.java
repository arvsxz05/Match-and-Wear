package com.example.arvin.matchandwear2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.arvin.matchandwear2.data.ClothesContract.ClothesEntry;
import com.example.arvin.matchandwear2.data.ClothesContract.OutfitEntry;
import com.example.arvin.matchandwear2.data.ClothesContract.UserEntry;

/**
 * Database helper for M&W app. Manages database creation and version management.
 */
public class ClothesDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ClothesDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "outfits.db";
    private static final int DATABASE_VERSION = 1;
    public ClothesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the clothes table
        String SQL_CREATE_CLOTHES_TABLE =  "CREATE TABLE " + ClothesEntry.TABLE_NAME + " ("
                + ClothesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ClothesEntry.COLUMN_CLOTH_NAME + " TEXT NOT NULL, "
                + ClothesEntry.COLUMN_CLOTH_TYPE + " INTEGER NOT NULL, "
                + ClothesEntry.COLUMN_CLOTH_PICTURE + " BLOB, "
                + ClothesEntry.COLUMN_CLOTH_OUTFIT + " INTEGER NOT NULL, "
                + "FOREIGN KEY (" + ClothesEntry.COLUMN_CLOTH_OUTFIT +") REFERENCES "+ OutfitEntry.TABLE_NAME+ "(" + OutfitEntry._ID + "));";

        // Create a String that contains the SQL statement to create the outfits table
        String SQL_CREATE_OUTFITS_TABLE =  "CREATE TABLE " + OutfitEntry.TABLE_NAME + " ("
                + OutfitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OutfitEntry.COLUMN_OUTFIT_NAME + " TEXT NOT NULL, "
                + OutfitEntry.COLUMN_OUTFIT_DATE + " INTEGER NOT NULL, "
                + OutfitEntry.COLUMN_OUTFIT_TYPE + " INTEGER NOT NULL)";

        // Create a String that contains the SQL statement to create the users table
        String SQL_CREATE_USERS_TABLE =  "CREATE TABLE " + UserEntry.TABLE_NAME + " ("
                + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserEntry.COLUMN_USER_FIRST_NAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_USER_LAST_NAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_USER_EMAIL + " TEXT NOT NULL, "
                + UserEntry.COLUMN_USER_USERNAME + " TEXT NOT NULL, "
                + UserEntry.COLUMN_USER_PASSWORD + " TEXT NOT NULL)";


        // Execute the SQL statement
        db.execSQL(SQL_CREATE_OUTFITS_TABLE);
        db.execSQL(SQL_CREATE_CLOTHES_TABLE);
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
