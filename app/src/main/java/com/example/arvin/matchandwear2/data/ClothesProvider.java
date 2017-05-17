package com.example.arvin.matchandwear2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.example.arvin.matchandwear2.data.ClothesContract.ClothesEntry;
import com.example.arvin.matchandwear2.data.ClothesContract.OutfitEntry;
import com.example.arvin.matchandwear2.data.ClothesContract.UserEntry;

/**
 * Created by User on 12/5/2016.
 */

public class ClothesProvider extends ContentProvider {
    public static final String LOG_TAG = ClothesProvider.class.getSimpleName();

    private static final int CLOTHES = 100;

    private static final int CLOTH_ID = 101;

    private static final int OUTFITS = 200;

    private static final int OUTFIT_ID = 201;

    private static final int USERS = 300;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.arvin.matchandwear2/clothes" will map to the
        // integer code {@link #CLOTHES}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(ClothesContract.CONTENT_AUTHORITY, ClothesContract.PATH_CLOTHES, CLOTHES);

        // The content URI of the form "content://com.example.arvin.matchandwear2/clothes/#" will map to the
        // integer code {@link #CLOTH_ID}. This URI is used to provide access to ONE single row
        // of the clothes table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.arvin.matchandwear2/clothes/3" matches, but
        // "content://com.example.arvin.matchandwear2/clothes" (without a number at the end) doesn't match.
        sUriMatcher.addURI(ClothesContract.CONTENT_AUTHORITY, ClothesContract.PATH_CLOTHES + "/#", CLOTH_ID);

        // Apply the same for the Owners table
        sUriMatcher.addURI(ClothesContract.CONTENT_AUTHORITY, ClothesContract.PATH_OUTFITS, OUTFITS);
        sUriMatcher.addURI(ClothesContract.CONTENT_AUTHORITY, ClothesContract.PATH_OUTFITS + "/#", OUTFIT_ID);

        sUriMatcher.addURI(ClothesContract.CONTENT_AUTHORITY, ClothesContract.PATH_USERS, USERS);
    }

    /** Database helper object */
    private ClothesDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ClothesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case CLOTHES:
                cursor = database.query(ClothesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CLOTH_ID:
                selection = ClothesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(ClothesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case OUTFITS:
                cursor = database.query(OutfitEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case OUTFIT_ID:
                selection = OutfitEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(OutfitEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case USERS:
                cursor = database.query(UserEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CLOTHES:
                return insertCloth(uri, contentValues);
            case OUTFITS:
                return insertOutfit(uri, contentValues);
            case USERS:
                return insertUser(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert an outfit into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertOutfit(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(OutfitEntry.COLUMN_OUTFIT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Outfit requires a name");
        }

        // Check that the type is valid
        Integer type = values.getAsInteger(OutfitEntry.COLUMN_OUTFIT_TYPE);
        if (type == null || !OutfitEntry.isValidType(type)) {
            throw new IllegalArgumentException("Outfit requires valid type");
        }

        Long date = values.getAsLong(OutfitEntry.COLUMN_OUTFIT_DATE);
        if (date == null || date < System.currentTimeMillis()) {
            throw new IllegalArgumentException("Outfit requires a valid date");
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new outfit with the given values
        long id = database.insert(OutfitEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a user into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertUser(Uri uri, ContentValues values) {
        // Check that the name is not null
        String first_name = values.getAsString(UserEntry.COLUMN_USER_FIRST_NAME);
        if (first_name == null) {
            throw new IllegalArgumentException("User requires a first name");
        }

        String last_name = values.getAsString(UserEntry.COLUMN_USER_LAST_NAME);
        if (last_name == null) {
            throw new IllegalArgumentException("User requires a last name");
        }

        // Check that the email is valid
        String email = values.getAsString(UserEntry.COLUMN_USER_EMAIL);
        if (email == null) {
            throw new IllegalArgumentException("User requires an email");
        }

        String username = values.getAsString(UserEntry.COLUMN_USER_USERNAME);
        if (username == null) {
            throw new IllegalArgumentException("User requires a username");
        }

        String password = values.getAsString(UserEntry.COLUMN_USER_PASSWORD);
        if (password == null) {
            throw new IllegalArgumentException("User requires a password");
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new user with the given values
        long id = database.insert(UserEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Insert a garment into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertCloth(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(ClothesEntry.COLUMN_CLOTH_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Cloth requires a name");
        }

        Integer type = values.getAsInteger(ClothesEntry.COLUMN_CLOTH_TYPE);
        if (type == null || !ClothesEntry.isValidType(type)) {
            throw new IllegalArgumentException("Outfit requires valid type");
        }

        Integer outfit_ref = values.getAsInteger(ClothesEntry.COLUMN_CLOTH_OUTFIT);
        if (outfit_ref != null && outfit_ref <= 0) {
            throw new IllegalArgumentException("Cloth requires valid outfit");
        }


        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new garment with the given values
        long id = database.insert(ClothesEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CLOTHES:
                return updateCloth(uri, contentValues, selection, selectionArgs);
            case CLOTH_ID:
                selection = ClothesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateCloth(uri, contentValues, selection, selectionArgs);
            case OUTFITS:
                return updateOutfit(uri, contentValues, selection, selectionArgs);
            case OUTFIT_ID:
                selection = OutfitEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateOutfit(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update garments in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more garments).
     * Return the number of rows that were successfully updated.
     */
    private int updateCloth(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ClothesEntry.COLUMN_CLOTH_NAME)) {
            String name = values.getAsString(ClothesEntry.COLUMN_CLOTH_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Cloth requires a name");
            }
        }

        if (values.containsKey(ClothesEntry.COLUMN_CLOTH_TYPE)) {
            Integer type = values.getAsInteger(ClothesEntry.COLUMN_CLOTH_TYPE);
            if (type == null || !ClothesEntry.isValidType(type)) {
                throw new IllegalArgumentException("Cloth requires valid type");
            }
        }

        if (values.containsKey(ClothesEntry.COLUMN_CLOTH_OUTFIT)) {
            Integer outfit_ref = values.getAsInteger(ClothesEntry.COLUMN_CLOTH_OUTFIT);
            if (outfit_ref != null && outfit_ref <= 0) {
                throw new IllegalArgumentException("Cloth requires valid outfit");
            }
        }

        // No need to check the picture, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(ClothesEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    /**
     * Update outfit in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more outfit).
     * Return the number of rows that were successfully updated.
     */
    private int updateOutfit(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // check that the name value is not null.
        if (values.containsKey(OutfitEntry.COLUMN_OUTFIT_NAME)) {
            String name = values.getAsString(OutfitEntry.COLUMN_OUTFIT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Outfit requires a name");
            }
        }

        // check that the type value is not null and is valid.
        if (values.containsKey(OutfitEntry.COLUMN_OUTFIT_TYPE)) {
            Integer type = values.getAsInteger(OutfitEntry.COLUMN_OUTFIT_TYPE);
            if (type == null || !OutfitEntry.isValidType(type)) {
                throw new IllegalArgumentException("Outfit requires valid type");
            }
        }

        if (values.containsKey(OutfitEntry.COLUMN_OUTFIT_DATE)) {
            Long address = values.getAsLong(OutfitEntry.COLUMN_OUTFIT_DATE);
            if (address == null || address < System.currentTimeMillis()) {
                throw new IllegalArgumentException("Outfit requires a valid date");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(OutfitEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CLOTHES:
                // Delete all rows that match the selection and selection args
                return database.delete(ClothesEntry.TABLE_NAME, selection, selectionArgs);
            case CLOTH_ID:
                // Delete a single row given by the ID in the URI
                selection = ClothesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return database.delete(ClothesEntry.TABLE_NAME, selection, selectionArgs);
            case OUTFITS:
                // Delete all rows that match the selection and selection args
                return database.delete(OutfitEntry.TABLE_NAME, selection, selectionArgs);
            case OUTFIT_ID:
                // Delete a single row given by the ID in the URI
                selection = OutfitEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return database.delete(OutfitEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CLOTHES:
                return ClothesEntry.CONTENT_LIST_TYPE;
            case CLOTH_ID:
                return ClothesEntry.CONTENT_ITEM_TYPE;
            case OUTFITS:
                return OutfitEntry.CONTENT_LIST_TYPE;
            case OUTFIT_ID:
                return OutfitEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}