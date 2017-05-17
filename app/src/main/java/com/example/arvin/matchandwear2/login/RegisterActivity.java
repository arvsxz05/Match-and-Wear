package com.example.arvin.matchandwear2.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arvin.matchandwear2.R;
import com.example.arvin.matchandwear2.data.ClothesContract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends Activity {

	EditText first, last, email, username, pass, confpass;
	Button save, cancel;
	SharedPreferences sharedpreferences;
	public static final String mypreference = "mypref";
	public static final String Username1 = "nameKey";
	public static final String Username2 = "emailKey";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);


		first = (EditText) findViewById(R.id.editfirstname);
		last = (EditText) findViewById(R.id.editlastname);
		email = (EditText) findViewById(R.id.editemail);
		username = (EditText) findViewById(R.id.editusername);
		pass = (EditText) findViewById(R.id.editpassword);
		confpass = (EditText) findViewById(R.id.editconformpassword);

		save = (Button) findViewById(R.id.btnsave);
		sharedpreferences = getSharedPreferences(mypreference,
				Context.MODE_PRIVATE);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// TODO Auto-generated method stub

				String edfirst = first.getText().toString();
				String edlast = last.getText().toString();
				String edemail = email.getText().toString();
				String edusername = username.getText().toString();
				String edpass = pass.getText().toString();
				String edConf = confpass.getText().toString();

				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putString(Username1,edfirst);
				editor.putString(Username2,edlast);
				editor.commit();


				//String StoredPassword =db.isEmail(edemail);

				if (edConf.equals(edpass) && isValidEmail(edemail) && isValidPassword(edpass))
				{
					ContentValues values = new ContentValues();
					values.put(ClothesContract.UserEntry.COLUMN_USER_FIRST_NAME, edfirst);
					values.put(ClothesContract.UserEntry.COLUMN_USER_LAST_NAME, edlast);
					values.put(ClothesContract.UserEntry.COLUMN_USER_EMAIL, edemail);
					values.put(ClothesContract.UserEntry.COLUMN_USER_USERNAME, edusername);
					values.put(ClothesContract.UserEntry.COLUMN_USER_PASSWORD, edpass);


					Uri newUri = getContentResolver().insert(ClothesContract.UserEntry.CONTENT_URI, values);

					if (newUri == null) {
						// If the new content URI is null, then there was an error with insertion.
						Toast.makeText(getApplicationContext(), "Registration Unsuccessful",
								Toast.LENGTH_SHORT).show();
					} else {
						// Otherwise, the insertion was successful and we can display a toast.
						Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();
						startActivity(new Intent(getApplicationContext(), LoginActivity.class));
					}
				}

				else  {

					email.setError("email must be .....@mail.com");
					pass.setError("Password must be longer than 6 sybmols");
					Toast.makeText(getApplicationContext(), "Empty Fields! ", Toast.LENGTH_SHORT).show();
				}
			}

			private boolean isValidEmail(String email) {
				String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

				Pattern pattern = Pattern.compile(EMAIL_PATTERN);
				Matcher matcher = pattern.matcher(email);
				return matcher.matches();
			}

			private boolean isValidPassword(String pass) {
				if (pass != null && pass.length() >= 6) {
					return true;
				}
				return false;
			}
		});

	}
}