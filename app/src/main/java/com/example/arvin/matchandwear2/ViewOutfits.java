package com.example.arvin.matchandwear2;

import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.arvin.matchandwear2.data.ClothesContract;
import com.example.arvin.matchandwear2.data.ClothesContract.OutfitEntry;
import com.example.arvin.matchandwear2.login.LoginActivity;

import java.util.Arrays;
import java.util.Date;

public class ViewOutfits extends AppCompatActivity {
    // an array of outfit details that is needed in our fragment
    static OutfitDetails[] detailsOutfit;
    static OutfitDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_outfits);

        displayDatabaseInfo();
    }

    // updates the fragment every time the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        displayDatabaseInfo();
    }

    // queries the database to load all the outfit for a specific date
    private void displayDatabaseInfo(){
        String[] projection = {
                OutfitEntry._ID,
                OutfitEntry.COLUMN_OUTFIT_NAME,
                OutfitEntry.COLUMN_OUTFIT_TYPE,
                OutfitEntry.COLUMN_OUTFIT_DATE};

        // Perform a query on the provider using the ContentResolver.
        String x = ((Long) getIntent().getLongExtra("date", -1)).toString();
        Date y = new Date(getIntent().getLongExtra("date", -1));
        Cursor cursor = getContentResolver().query(
                OutfitEntry.CONTENT_URI,   // The content URI of the words table
                projection,             // The columns to return for each row
                String.format("%s = ?", OutfitEntry.COLUMN_OUTFIT_DATE),      // Selection criteria
                new String[]{x},                   // Selection criteria
                null);                  // The sort order for the returned rows
        try {

            int idColumnIndex = cursor.getColumnIndex(OutfitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_NAME);
            int typeColumnIndex = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_TYPE);
            int dateColumnIndex = cursor.getColumnIndex(OutfitEntry.COLUMN_OUTFIT_DATE);

            int i=0;
            detailsOutfit = new OutfitDetails[cursor.getCount()];

            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                Integer currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                Integer currentType = cursor.getInt(typeColumnIndex);
                Long z = cursor.getLong(dateColumnIndex);
                Date currentDate = new Date(z);
                detailsOutfit[i] = new OutfitDetails(currentID, currentName, currentType, currentDate);
                i++;
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_logout){
            Intent intent = new Intent(ViewOutfits.this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // contains the list of outfit on a specific date
    public static class TitlesFragment extends ListFragment {

        public TitlesFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            adapter = new OutfitDetailsAdapter(getActivity(), Arrays.asList(detailsOutfit));
            // Populate list with our static array of titles.
            setListAdapter(adapter);
        }

        @Override
        public void onResume() {
            adapter = new OutfitDetailsAdapter(getActivity(), Arrays.asList(detailsOutfit));
            // Populate list with our static array of titles.
            setListAdapter(adapter);
            super.onResume();
        }
    }
}
