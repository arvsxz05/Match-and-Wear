package com.example.arvin.matchandwear2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the M&W app.
 */
public final class ClothesContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ClothesContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.
     **/
    public static final String CONTENT_AUTHORITY = "com.example.arvin.matchandwear2";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible paths (appended to base content URI for possible URI's)
     * For instance, content://com.example.arvin.matchandwear2/clothes/ is a valid path for
     * looking at clothes data.
     **/
    public static final String PATH_CLOTHES = "clothes";
    public static final String PATH_USERS = "users";
    public static final String PATH_OUTFITS = "outfits";

    /**
     * Inner class that defines constant values for the garments database table.
     * Each entry in the table represents a single garment.
     */
    public static final class ClothesEntry implements BaseColumns {

        /** The content URI to access the garment data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CLOTHES);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of garments.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLOTHES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single garment.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLOTHES;

        /** Name of database table for clothes */
        public final static String TABLE_NAME = "clothes";

        /**
         * Unique ID number for the garment (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the garment.
         *
         * Type: TEXT
         */
        public final static String COLUMN_CLOTH_NAME ="name";

        /**
         * Type of the garment.
         *
         * The only possible values are {@link #TYPE_TOP}, {@link #TYPE_BOTTOM},
         * or {@link #TYPE_FOOTWEAR}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_CLOTH_TYPE = "cloth_type";

        /**
         * Picture of the garment.
         *
         * Type: BLOB
         */
        public final static String COLUMN_CLOTH_PICTURE = "picture";

        /**
         * Foreign key column that maps a garment to an outfit.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_CLOTH_OUTFIT = "outfit_refer";

        /**
         * Possible values for the type of the garment.
         */
        public static final int TYPE_TOP = 0;
        public static final int TYPE_BOTTOM = 1;
        public static final int TYPE_FOOTWEAR = 2;

        /**
         * Returns whether or not the given type is {@link #TYPE_TOP}, {@link #TYPE_BOTTOM},
         * or {@link #TYPE_FOOTWEAR}.
         */
        public static boolean isValidType(int type) {
            if (type == TYPE_TOP || type == TYPE_BOTTOM || type == TYPE_FOOTWEAR) {
                return true;
            }
            return false;
        }
    }

    /**
     * Inner class that defines constant values for the outfits database table.
     * Each entry in the table represents a single outfit.
     */
    public static final class OutfitEntry implements BaseColumns {

        /** Same explanation to the prevoius table */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_OUTFITS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OUTFITS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OUTFITS;
        public final static String TABLE_NAME = "outfits";

        /**
         * Unique ID number for the outfit (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the outfit.
         *
         * Type: TEXT
         */
        public final static String COLUMN_OUTFIT_NAME ="name";

        /**
         * Type of the outfit.
         *
         * The only possible values are {@link #TYPE_CASUAL}, {@link #TYPE_TRENDY}, {@link #TYPE_ELEGANT},
         * {@link #TYPE_SEXY}, {@link #TYPE_EXOTIC}, or {@link #TYPE_OTHERS}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_OUTFIT_TYPE = "type";

        /**
         * Date an outfit is scheduled to be worn represented as milliseconds.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_OUTFIT_DATE = "date";

        /**
         * Possible values for the type of the outfit.
         */
        public static final int TYPE_CASUAL = 0;
        public static final int TYPE_TRENDY = 1;
        public static final int TYPE_ELEGANT = 2;
        public static final int TYPE_SEXY = 3;
        public static final int TYPE_EXOTIC = 4;
        public static final int TYPE_OTHERS = 5;

        /**
         * Returns whether or not the given type is {@link #TYPE_CASUAL}, {@link #TYPE_TRENDY}, {@link #TYPE_ELEGANT},
         * {@link #TYPE_SEXY}, {@link #TYPE_EXOTIC}, or {@link #TYPE_OTHERS}.
         */
        public static boolean isValidType(int type) {
            if (type == TYPE_CASUAL || type == TYPE_TRENDY || type == TYPE_ELEGANT
                    || type == TYPE_SEXY || type == TYPE_EXOTIC || type == TYPE_OTHERS) {
                return true;
            }
            return false;
        }
    }

    /**
     * Inner class that defines constant values for the users database table.
     * Each entry in the table represents a single pet.
     */
    public static final class UserEntry implements BaseColumns {

        /** Same explanation to the previous table */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USERS;
        public final static String TABLE_NAME = "users";

        /**
         * Unique ID number for the users (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * First name of the user.
         *
         * Type: TEXT
         */
        public final static String COLUMN_USER_FIRST_NAME ="first_name";

        /**
         * Last name of the user.
         *
         * Type: TEXT
         */
        public final static String COLUMN_USER_LAST_NAME = "last_name";

        /**
         * Email of the user.
         *
         * Type: TEXT
         */
        public final static String COLUMN_USER_EMAIL = "email";

        /**
         * Username of the user.
         *
         * Type: TEXT
         */
        public final static String COLUMN_USER_USERNAME = "username";

        /**
         * Password of the user.
         *
         * Type: TEXT
         */
        public final static String COLUMN_USER_PASSWORD= "password";
    }

}
