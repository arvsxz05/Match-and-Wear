package com.example.arvin.matchandwear2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arvin.matchandwear2.data.ClothesContract;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class UpdateOutfit extends AppCompatActivity {

    // request codes to let us know the result of the intent that is being returned from the activiity that has been called like pictures from the gallery
    private static final int PICK_IMAGE_TOP = 100;
    private static final int PICK_IMAGE_BOTTOM = 200;
    private static final int PICK_IMAGE_FOOTWEAR = 300;
    private static final int CAMERA_REQUEST_TOP = 1888;
    private static final int CAMERA_REQUEST_BOTTOM = 1889;
    private static final int CAMERA_REQUEST_FOOTWEAR = 1890;

    EditText updateOutfitName; // edittext field name - new name to be set
    Spinner updateSpinnerType; // outfit type dropdown

    TextView date; // datepicker

    ImageView updateTop, updateBottom, updateFootwear;

    Button uploadTop, uploadBottom, uploadFootwear, saveUpdates, takePicTop, takePicBottom, takePicFootwear; // buttond used whether choose from gallery or take new pic

    int selectedType;
    Integer topId, bottomId, footwearId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_outfit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date = (TextView) findViewById(R.id.update_outfit_date); // datepicker

        updateTop = (ImageView) findViewById(R.id.update_imageTop); //update top button
        uploadTop = (Button) findViewById(R.id.update_uploadPictureTop); // shows/renders the top outfit previously uploaded

        updateBottom = (ImageView) findViewById(R.id.update_imageBottom); //update bottom button
        uploadBottom = (Button) findViewById(R.id.update_uploadPictureBottom); // shows/renders the botto, outfit previously uploaded

        updateFootwear = (ImageView) findViewById(R.id.update_imageFootwear); // update footwear button
        uploadFootwear = (Button) findViewById(R.id.update_uploadPictureFootwear); // shows/renders the footwear outfit previously uploaded

        updateSpinnerType = (Spinner) findViewById(R.id.update_spinner_outfit_type); //outfit type dropdown
        updateOutfitName = (EditText) findViewById(R.id.update_outfit_name); // update ourfit name
        saveUpdates = (Button) findViewById(R.id.submit_outfit_updates); // Save button

        takePicTop = (Button) findViewById(R.id.update_takePictureTop); // button to 'take picture' top outfit
        takePicBottom = (Button) findViewById(R.id.update_takePictureBottom); // button to 'take picture' bottom outfit
        takePicFootwear = (Button) findViewById(R.id.update_takePictureFootwear); // button to 'take picture' footwear outfit


        selectedType = getIntent().getIntExtra("type", -1); // set new type

        date.setText((new Date(getIntent().getLongExtra("date", -1))).toString()); // set new date
        updateOutfitName.setText(getIntent().getStringExtra("name")); // update name

        setUpPics(); // renders pics

        takePicTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_TOP); // start device camera widget
            }
        }); // redirects to camera widget - has option to take pic of top outfit

        takePicBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_BOTTOM); // start device camera
            }
        }); // redirects to camera widget - has option to take pic of bottom outfit

        takePicFootwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_FOOTWEAR); // start device camera
            }
        }); // redirects to camera widget - has option to take pic of footwear outfit

        uploadTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery("Top");
            }
        }); // redirects to gallery once "Choose from Gallery is selcted";
        uploadBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery("Bottom");
            }
        }); // redirects to gallery once "Choose from Gallery is selcted";
        uploadFootwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery("Footwear");
            }
        });// redirects to gallery once "Choose from Gallery is selcted";

        setupSpinner();

        saveUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateOutfitName.getText() == null || updateOutfitName.getText().toString().trim() == "") {
                    Toast.makeText(getApplicationContext(), "Outfit Name is empty", Toast.LENGTH_LONG).show();
                } // error catch, cannot proceed when outfit name is empty
                else {
                    updateOutfit();
                    updateTopCloth();
                    updateBottomCloth();
                    updateFootwearCloth();
                    finish();
                }
            }
        });
    }

    private void setUpPics() {
        String[] projection = {
                ClothesContract.ClothesEntry._ID,
                ClothesContract.ClothesEntry.COLUMN_CLOTH_TYPE,
                ClothesContract.ClothesEntry.COLUMN_CLOTH_OUTFIT,
                ClothesContract.ClothesEntry.COLUMN_CLOTH_PICTURE};

        // Perform a query on the provider using the ContentResolver.
        Cursor cursor = getContentResolver().query(
                ClothesContract.ClothesEntry.CONTENT_URI,   // The content URI of the words table
                projection,             // The columns to return for each row
                String.format("%s = ?", ClothesContract.ClothesEntry.COLUMN_CLOTH_OUTFIT),  // Selection criteria
                new String[]{((Integer) getIntent().getIntExtra("id", -1)).toString()},     // Selection criteria
                null);                  // The sort order
        // for the returned rows
        try {
            int idColumnIndex = cursor.getColumnIndex(ClothesContract.ClothesEntry._ID);
            int typeColumnIndex = cursor.getColumnIndex(ClothesContract.ClothesEntry.COLUMN_CLOTH_TYPE);
            int outfitColumnIndex = cursor.getColumnIndex(ClothesContract.ClothesEntry.COLUMN_CLOTH_OUTFIT);
            int pictureColumnIndex = cursor.getColumnIndex(ClothesContract.ClothesEntry.COLUMN_CLOTH_PICTURE);

            while (cursor.moveToNext()) {
                Integer currentId = cursor.getInt(idColumnIndex);
                Integer currentType = cursor.getInt(typeColumnIndex);
                Integer currentOutfit = cursor.getInt(outfitColumnIndex);
                byte[] currentPicture = cursor.getBlob(pictureColumnIndex);

                if(getIntent().getIntExtra("id", -1) == currentOutfit) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(currentPicture, 0, currentPicture.length);
                    if (currentType == 0){
                        topId = currentId;
                        updateTop.setImageBitmap(bmp);}
                    if (currentType == 1){
                        bottomId = currentId;
                        updateBottom.setImageBitmap(bmp);}
                    if (currentType == 2){
                        footwearId = currentId;
                        updateFootwear.setImageBitmap(bmp);}
                }
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * sets up the spinner for the dropdown of types for outfit
     * set in the UI
     **/
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter outfitTypeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_outfit_type_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        outfitTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        updateSpinnerType.setAdapter(outfitTypeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        updateSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.outfit_type_casual))) {
                        selectedType = ClothesContract.OutfitEntry.TYPE_CASUAL;
                    } else if (selection.equals(getString(R.string.outfit_type_trendy))) {
                        selectedType = ClothesContract.OutfitEntry.TYPE_TRENDY;
                    } else if (selection.equals(getString(R.string.outfit_type_elegant))) {
                        selectedType = ClothesContract.OutfitEntry.TYPE_ELEGANT;
                    } else if (selection.equals(getString(R.string.outfit_type_sexy))) {
                        selectedType = ClothesContract.OutfitEntry.TYPE_SEXY;
                    } else if (selection.equals(getString(R.string.outfit_type_exotic))) {
                        selectedType = ClothesContract.OutfitEntry.TYPE_EXOTIC;
                    } else {
                        selectedType = ClothesContract.OutfitEntry.TYPE_OTHERS;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedType = ClothesContract.OutfitEntry.TYPE_CASUAL;
            }
        });
    }

    /**
     * opens the gallery in different conditions, setting up different request codes for different purposes,
     * whether we select an image for top, bottom, or footwear
     **/
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

    /**
     * sets up the image, either from gallery or from camera, to the imageview we have defined in our UI
     * based on the request codes that they have returned
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_TOP) {
                Uri selectedImage = data.getData();
                updateTop.setImageURI(selectedImage);
            }
            if (requestCode == PICK_IMAGE_BOTTOM) {
                Uri selectedImage = data.getData();
                updateBottom.setImageURI(selectedImage);
            }
            if (requestCode == PICK_IMAGE_FOOTWEAR) {
                Uri selectedImage = data.getData();
                updateFootwear.setImageURI(selectedImage);
            }
            if (requestCode == CAMERA_REQUEST_TOP) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                updateTop.setImageBitmap(photo);
            }
            if (requestCode == CAMERA_REQUEST_BOTTOM) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                updateBottom.setImageBitmap(photo);
            }
            if (requestCode == CAMERA_REQUEST_FOOTWEAR) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                updateFootwear.setImageBitmap(photo);
            }
        }
    }

    /**
     * creates a request to the content provider to
     * update given values in the ContentValues to the outfit that fall
     * under the arguments set in the selection arguments (ID)
     **/
    public void updateOutfit(){
        String nameString = updateOutfitName.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(ClothesContract.OutfitEntry.COLUMN_OUTFIT_NAME, nameString);
        values.put(ClothesContract.OutfitEntry.COLUMN_OUTFIT_TYPE, selectedType);


        int i = getContentResolver().update(ClothesContract.OutfitEntry.CONTENT_URI, values,
                String.format("%s = ?", ClothesContract.OutfitEntry._ID), new String[]{((Integer)getIntent().getIntExtra("id", -1)).toString()});
        if(i != 0){
            Toast.makeText(this, getString(R.string.update_outfit_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * creates a request to the content provider to
     * update given values in the ContentValues to the top garment that fall
     * under the arguments set in the selection arguments (ID)
     **/
    public void updateTopCloth(){
        String nameTopString = updateOutfitName.getText().toString().trim() + "Top";

        BitmapDrawable drawable = (BitmapDrawable) updateTop.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        ContentValues values = new ContentValues();
        values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_NAME, nameTopString);
        values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_PICTURE, imageInByte);


        int i = getContentResolver().update(ClothesContract.ClothesEntry.CONTENT_URI, values, String.format("%s = ?", ClothesContract.ClothesEntry._ID), new String[]{topId.toString()});
        if(i != 0){
            Toast.makeText(this, getString(R.string.update_outfit_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * creates a request to the content provider to
     * update given values in the ContentValues to the bottom garment that fall
     * under the arguments set in the selection arguments (ID)
     **/
    public void updateBottomCloth(){
        String nameBottomString = updateOutfitName.getText().toString().trim() + "Bottom";

        BitmapDrawable drawable = (BitmapDrawable) updateBottom.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        ContentValues values = new ContentValues();
        values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_NAME, nameBottomString);
        values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_PICTURE, imageInByte);

        int i = getContentResolver().update(ClothesContract.ClothesEntry.CONTENT_URI, values, String.format("%s = ?", ClothesContract.ClothesEntry._ID), new String[]{bottomId.toString()});
        if(i != 0){
            Toast.makeText(this, getString(R.string.update_outfit_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * creates a request to the content provider to
     * update given values in the ContentValues to the footwear garment that fall
     * under the arguments set in the selection arguments (ID)
     **/
    public void updateFootwearCloth(){
        String nameFootwearString = updateOutfitName.getText().toString().trim() + "Footwear";

        BitmapDrawable drawable = (BitmapDrawable) updateFootwear.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();

        ContentValues values = new ContentValues();
        values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_NAME, nameFootwearString);
        values.put(ClothesContract.ClothesEntry.COLUMN_CLOTH_PICTURE, imageInByte);


        int i = getContentResolver().update(ClothesContract.ClothesEntry.CONTENT_URI, values, String.format("%s = ?", ClothesContract.ClothesEntry._ID), new String[]{footwearId.toString()});
        if(i != 0){
            Toast.makeText(this, getString(R.string.update_outfit_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
