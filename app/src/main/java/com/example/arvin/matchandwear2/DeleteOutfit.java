package com.example.arvin.matchandwear2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.arvin.matchandwear2.data.ClothesContract.OutfitEntry;
import com.example.arvin.matchandwear2.data.ClothesContract.ClothesEntry;

public class DeleteOutfit extends AppCompatActivity {

    Button yes, no; // selection for the action "Are you sure you want to delete this outfit?"
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_outfit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // displays actionbar

        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        name = (TextView) findViewById(R.id.outfit_name_delete);

        // getting of the data passed through intent, in this case, the name
        // and setting it in the TextView defined in the XML
        name.setText(getIntent().getStringExtra("name"));

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            } // once no is clicked, nothing happens.
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = getContentResolver().delete(OutfitEntry.CONTENT_URI, String.format("%s = ?", OutfitEntry._ID), new String[]{((Integer)getIntent().getIntExtra("id", -1)).toString()});
                int j = getContentResolver().delete(ClothesEntry.CONTENT_URI, String.format("%s = ?", ClothesEntry.COLUMN_CLOTH_OUTFIT), new String[]{((Integer)getIntent().getIntExtra("id", -1)).toString()});
                finish();
            } // once yes is chosen, the id of the outfit to be deleted is passed and then queried from the database and then removed from there. (through content provider)
        });
    }

}
