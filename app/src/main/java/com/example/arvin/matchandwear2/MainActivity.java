package com.example.arvin.matchandwear2;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arvin.matchandwear2.data.ClothesContract.ClothesEntry;
import com.example.arvin.matchandwear2.data.ClothesContract.OutfitEntry;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView imageTop;
    Button uploadTop;
    ImageView imageBottom;
    Button uploadBottom;
    ImageView imageFootwear;
    Button uploadFootwear;
    Spinner mOutfitTypeSpinner;

    EditText outfitName;
    EditText dateText;

    Button saveOutfit;

    Uri newOutfitUri;

    Integer mOutfit = OutfitEntry.TYPE_CASUAL;

    private static final int PICK_IMAGE_TOP = 100;
    private static final int PICK_IMAGE_BOTTOM = 200;
    private static final int PICK_IMAGE_FOOTWEAR = 300;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageTop = (ImageView) findViewById(R.id.imageTop);
        uploadTop = (Button) findViewById(R.id.uploadPictureTop);

        imageBottom = (ImageView) findViewById(R.id.imageBottom);
        uploadBottom = (Button) findViewById(R.id.uploadPictureBottom);

        imageFootwear = (ImageView) findViewById(R.id.imageFootwear);
        uploadFootwear = (Button) findViewById(R.id.uploadPictureFootwear);

        dateText = (EditText) findViewById(R.id.outfit_date);
        mOutfitTypeSpinner = (Spinner) findViewById(R.id.spinner_outfit_type);
        outfitName = (EditText) findViewById(R.id.outfit_name);
        saveOutfit = (Button) findViewById(R.id.submit_outfit);

        dateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        uploadTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery("Top");
            }
        });
        uploadBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery("Bottom");
            }
        });
        uploadFootwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery("Footwear");
            }
        });

        mOutfitTypeSpinner = (Spinner) findViewById(R.id.spinner_outfit_type);

        setupSpinner();

        saveOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertOutfit();
                insertTopCloth();
                insertBottomCloth();
                insertFootwearCloth();
                finish();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateText.setText(sdf.format(myCalendar.getTime()));
    }

    private void openGallery(String type) {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        if(type == "Top")
            startActivityForResult(Intent.createChooser(gallery, "Select Photos: "), PICK_IMAGE_TOP);
        if(type == "Bottom")
            startActivityForResult(Intent.createChooser(gallery, "Select Photos: "), PICK_IMAGE_BOTTOM);
        if(type == "Footwear")
            startActivityForResult(Intent.createChooser(gallery, "Select Photos: "), PICK_IMAGE_FOOTWEAR);
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter outfitTypeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_outfit_type_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        outfitTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mOutfitTypeSpinner.setAdapter(outfitTypeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mOutfitTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.outfit_type_casual))) {
                        mOutfit = OutfitEntry.TYPE_CASUAL;
                    } else if (selection.equals(getString(R.string.outfit_type_trendy))) {
                        mOutfit = OutfitEntry.TYPE_TRENDY;
                    } else if (selection.equals(getString(R.string.outfit_type_elegant))) {
                        mOutfit = OutfitEntry.TYPE_ELEGANT;
                    } else if (selection.equals(getString(R.string.outfit_type_sexy))) {
                        mOutfit = OutfitEntry.TYPE_SEXY;
                    } else if (selection.equals(getString(R.string.outfit_type_exotic))) {
                        mOutfit = OutfitEntry.TYPE_EXOTIC;
                    } else {
                        mOutfit = OutfitEntry.TYPE_OTHERS;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mOutfit = OutfitEntry.TYPE_CASUAL;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_TOP) {
                Uri selectedImage = data.getData();
                imageTop.setImageURI(selectedImage);
            }
            if (requestCode == PICK_IMAGE_BOTTOM) {
                Uri selectedImage = data.getData();
                imageBottom.setImageURI(selectedImage);
            }
            if (requestCode == PICK_IMAGE_FOOTWEAR) {
                Uri selectedImage = data.getData();
                imageFootwear.setImageURI(selectedImage);
            }
        }
    }

    public void insertOutfit(){
        String nameString = outfitName.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(OutfitEntry.COLUMN_OUTFIT_NAME, nameString);
        values.put(OutfitEntry.COLUMN_OUTFIT_TYPE, mOutfit);
        values.put(OutfitEntry.COLUMN_OUTFIT_DATE, myCalendar.getTimeInMillis());

        newOutfitUri = getContentResolver().insert(OutfitEntry.CONTENT_URI, values);

        if (newOutfitUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.insert_outfit_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.insert_outfit_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void insertTopCloth(){
        String nameTopString = outfitName.getText().toString().trim() + "Top";

        BitmapDrawable drawable = (BitmapDrawable) imageTop.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        ContentValues values = new ContentValues();
        values.put(ClothesEntry.COLUMN_CLOTH_NAME, nameTopString);
        values.put(ClothesEntry.COLUMN_CLOTH_TYPE, 0);
        values.put(ClothesEntry.COLUMN_CLOTH_PICTURE, imageInByte);
        values.put(ClothesEntry.COLUMN_CLOTH_OUTFIT, ContentUris.parseId(newOutfitUri));

        Uri newUri = getContentResolver().insert(ClothesEntry.CONTENT_URI, values);

        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.insert_outfit_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.insert_outfit_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void insertBottomCloth(){
        String nameBottomString = outfitName.getText().toString().trim() + "Bottom";

        BitmapDrawable drawable = (BitmapDrawable) imageBottom.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        ContentValues values = new ContentValues();
        values.put(ClothesEntry.COLUMN_CLOTH_NAME, nameBottomString);
        values.put(ClothesEntry.COLUMN_CLOTH_TYPE, 1);
        values.put(ClothesEntry.COLUMN_CLOTH_PICTURE, imageInByte);
        values.put(ClothesEntry.COLUMN_CLOTH_OUTFIT, ContentUris.parseId(newOutfitUri));

        Uri newUri = getContentResolver().insert(ClothesEntry.CONTENT_URI, values);

        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.insert_outfit_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.insert_outfit_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void insertFootwearCloth(){
        String nameFootwearString = outfitName.getText().toString().trim() + "Footwear";

        BitmapDrawable drawable = (BitmapDrawable) imageFootwear.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        ContentValues values = new ContentValues();
        values.put(ClothesEntry.COLUMN_CLOTH_NAME, nameFootwearString);
        values.put(ClothesEntry.COLUMN_CLOTH_TYPE, 2);
        values.put(ClothesEntry.COLUMN_CLOTH_PICTURE, imageInByte);
        values.put(ClothesEntry.COLUMN_CLOTH_OUTFIT, ContentUris.parseId(newOutfitUri));

        Uri newUri = getContentResolver().insert(ClothesEntry.CONTENT_URI, values);

        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.insert_outfit_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.insert_outfit_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }



}
