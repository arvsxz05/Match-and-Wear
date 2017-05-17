package com.example.arvin.matchandwear2.login;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arvin.matchandwear2.FirstActivity;
import com.example.arvin.matchandwear2.OutfitDetails;
import com.example.arvin.matchandwear2.OutfitDetailsAdapter;
import com.example.arvin.matchandwear2.R;
import com.example.arvin.matchandwear2.data.ClothesContract;


public class LoginActivity extends Activity {


	ImageView logo;
	TextView name; // initializing the variables based on the xml
	EditText user, pass; // initializing the variables based on the xml
	Button login, not_reg; // initializing the variables based on the xml


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		logo = (ImageView) findViewById(R.id.app_logo);
		name = (TextView) findViewById(R.id.app_name);

		user =(EditText)findViewById(R.id.eduser);
		pass = (EditText)findViewById(R.id.edpass);
		login =(Button)findViewById(R.id.login);
		not_reg =(Button)findViewById(R.id.not_reg);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String username = user.getText().toString(); // extracting the string of editText user and storing it to username for future use
				String password = pass.getText().toString(); // extracting the string of editPass user and storing it to password for future use
				String[] projection = {
						ClothesContract.UserEntry.COLUMN_USER_USERNAME,
						ClothesContract.UserEntry.COLUMN_USER_PASSWORD};

				// Perform a query on the provider using the ContentResolver.
				Cursor cursor = getContentResolver().query(
						ClothesContract.UserEntry.CONTENT_URI,  		 // The content URI of the words table
						projection,            							 // The columns to return for each row
						"username=?",            				         // Selection criteria; WHERE clause
						new String[]{username},                 	     // Selection criteria
						null);                 							 // The sort order for the returned rows

				int usernameColumnIndex = cursor.getColumnIndex(ClothesContract.UserEntry.COLUMN_USER_USERNAME);
				int passwordColumnIndex = cursor.getColumnIndex(ClothesContract.UserEntry.COLUMN_USER_PASSWORD);
				boolean found = false;
				while (cursor.moveToNext()) {
					// Use that index to extract the String or Int value of the word
					// at the current row the cursor is on.
					if(username.equals(cursor.getString(usernameColumnIndex))
							&& password.equals(cursor.getString(passwordColumnIndex))){
						found = true;
					} // this chunk of if-statement tries to see if the user who logged in matches what is inside the databae
					// if found, boolean var found is set to true meaning user is identified and granted access/
				}
				if(found == true){
					Toast.makeText(getApplicationContext(),"You have successfully entered ", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(LoginActivity.this, FirstActivity.class);
					startActivity(intent);
				} // Pop-up to let the user know that the username and password match
				else{
					Toast.makeText(getApplicationContext(), "Username/Password incorrect", Toast.LENGTH_LONG).show();
					user.setText("");
					pass.setText("");
				} // Pop-up to let the user know that the username and password entered do not match
			}
		});

		not_reg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
			} // once logged-in, redirected to the view calendar page
		});
	}
}
		

