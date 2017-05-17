package com.example.arvin.matchandwear2;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 12/5/2016.
 */

public class OutfitDetailsAdapter extends ArrayAdapter<OutfitDetails> {
    private static final String LOG_TAG = OutfitDetailsAdapter.class.getSimpleName();

    public OutfitDetailsAdapter(Activity context, List<OutfitDetails> details) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and 3 buttons, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, details);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the OutfitDetails object from the ArrayAdapter at the appropriate position
        final OutfitDetails individualOutfit = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_outfit_details, parent, false);
        }
        if(individualOutfit == null) {
            return convertView;
        }

        TextView bookNameView = (TextView) convertView.findViewById(R.id.list_item_title);
        bookNameView.setText(individualOutfit.outfit_name);

        TextView bookAuthorView = (TextView) convertView.findViewById(R.id.list_item_dialogue);
        bookAuthorView.setText(individualOutfit.date.toString());

        // If the edit button is being clicked, we are going to send all the necessary date into the
        // UpdateOutfit activity for the user to edit necessary details about the outfit
        Button edits = (Button) convertView.findViewById(R.id.edit_outfit);
        edits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("id", individualOutfit.id);
                intent.putExtra("name", individualOutfit.outfit_name);
                intent.putExtra("type", individualOutfit.type);
                intent.putExtra("date", individualOutfit.date.getTime() - individualOutfit.date.getTime()%1000);
                intent.setClass(getContext(), UpdateOutfit.class);
                getContext().startActivity(intent);
            }
        });

        // If the delete button is being clicked, we are go to an activity where the user confirms
        // the deletion of an outfit
        Button delete = (Button) convertView.findViewById(R.id.delete_outfit);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("id", individualOutfit.id);
                intent.putExtra("name", individualOutfit.outfit_name);
                intent.setClass(getContext(), DeleteOutfit.class);
                getContext().startActivity(intent);
            }
        });

        // If the view button is being clicked, we are going to send all the necessary date into the
        // ViewOutfitDetails activity for the user to view necessary details about the outfit
        Button viewer = (Button) convertView.findViewById(R.id.view_outfit);
        viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("id", individualOutfit.id);
                intent.putExtra("name", individualOutfit.outfit_name);
                intent.putExtra("type", individualOutfit.type);
                intent.putExtra("date", individualOutfit.date.getTime() - individualOutfit.date.getTime()%1000);
                intent.setClass(getContext(), ViewOutfitDetails.class);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}

