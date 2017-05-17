package com.example.arvin.matchandwear2;

import android.content.ContentProvider;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arvin.matchandwear2.data.ClothesContract.ClothesEntry;
import com.example.arvin.matchandwear2.data.ClothesProvider;

import java.util.Date;

public class ViewOutfitDetails extends AppCompatActivity {

    TextView name;
    TextView type;
    TextView date;

    ImageView top, bottom, footwear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_outfit_details);

        name = (TextView) findViewById(R.id.outfit_name_detail);
        type = (TextView) findViewById(R.id.outfit_type_detail);
        date = (TextView) findViewById(R.id.outfit_date_detail);

        top = (ImageView) findViewById(R.id.imageViewTopDetail);
        bottom = (ImageView) findViewById(R.id.imageViewBottomDetail);
        footwear = (ImageView) findViewById(R.id.imageViewFootwearDetail);

        name.setText(getIntent().getStringExtra("name"));
        date.setText(getIntent().getStringExtra("date"));

        switch (getIntent().getIntExtra("type", 0)) {
            case 0:
                type.setText(getString(R.string.outfit_type_casual));
                break;
            case 1:
                type.setText(getString(R.string.outfit_type_trendy));
                break;
            case 2:
                type.setText(getString(R.string.outfit_type_elegant));
                break;
            case 3:
                type.setText(getString(R.string.outfit_type_sexy));
                break;
            case 4:
                type.setText(getString(R.string.outfit_type_exotic));
                break;
            case 5:
                type.setText(getString(R.string.outfit_type_others));
                break;
        }

        retrieveImages();
    }

    void retrieveImages() {
        String[] projection = {
                ClothesEntry._ID,
                ClothesEntry.COLUMN_CLOTH_NAME,
                ClothesEntry.COLUMN_CLOTH_TYPE,
                ClothesEntry.COLUMN_CLOTH_OUTFIT,
                ClothesEntry.COLUMN_CLOTH_PICTURE};

        // Perform a query on the provider using the ContentResolver.
        Cursor cursor = getContentResolver().query(
                ClothesEntry.CONTENT_URI,   // The content URI of the words table
                projection,             // The columns to return for each row
                String.format("%s = ?", ClothesEntry.COLUMN_CLOTH_OUTFIT),                   // Selection criteria
                new String[]{((Integer) getIntent().getIntExtra("id", -1)).toString()},                   // Selection criteria
                null);                  // The sort order
        // for the returned rows
        try {
            int nameColumnIndex = cursor.getColumnIndex(ClothesEntry.COLUMN_CLOTH_NAME);
            int typeColumnIndex = cursor.getColumnIndex(ClothesEntry.COLUMN_CLOTH_TYPE);
            int outfitColumnIndex = cursor.getColumnIndex(ClothesEntry.COLUMN_CLOTH_OUTFIT);
            int pictureColumnIndex = cursor.getColumnIndex(ClothesEntry.COLUMN_CLOTH_PICTURE);

            while (cursor.moveToNext()) {
                String currentName = cursor.getString(nameColumnIndex);
                Integer currentType = cursor.getInt(typeColumnIndex);
                Integer currentOutfit = cursor.getInt(outfitColumnIndex);
                byte[] currentPicture = cursor.getBlob(pictureColumnIndex);

                if(getIntent().getIntExtra("id", -1) == currentOutfit) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(currentPicture, 0, currentPicture.length);
                    if (currentType == 0)
                        top.setImageBitmap(bmp);
                    if (currentType == 1)
                        bottom.setImageBitmap(bmp);
                    if (currentType == 2)
                        footwear.setImageBitmap(bmp);
                }
            }
        } finally {
            cursor.close();
        }
    }
}
