package com.chowlb.keystore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AddPasswordActivity extends Activity {

	EditText name;
	EditText username;
	EditText password;
	EditText website;
	EditText description;
	String nameValue;
	String usernameValue;
	String passwordValue;
	String websiteValue;
	String descriptionValue;
	DatabaseHandler db;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_password);
		
		db = new DatabaseHandler(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_password, menu);
		return true;
	}
	
	public void submit(View v) {
		name = (EditText) findViewById(R.id.nameEditText);
		username = (EditText) findViewById(R.id.usernameEditText);
		password = (EditText) findViewById(R.id.passwordEditText);
		website = (EditText) findViewById(R.id.websiteEditText);
		description = (EditText) findViewById(R.id.descriptionEditText);
		
		nameValue = name.getText().toString();
		usernameValue = username.getText().toString();
		passwordValue = password.getText().toString();
		websiteValue = website.getText().toString();
		descriptionValue = description.getText().toString();
		
		db.addPasswordEntry(new PasswordEntry(nameValue, usernameValue, passwordValue, websiteValue, descriptionValue));
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}

}
