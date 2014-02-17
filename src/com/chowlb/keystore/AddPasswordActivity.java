package com.chowlb.keystore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPasswordActivity extends Activity implements OnClickListener{

	EditText name;
	EditText username;
	EditText password;
	EditText website;
	EditText description;
	
	String nameValue;
	String originalName;
	String usernameValue;
	String passwordValue;
	String websiteValue;
	String descriptionValue;
	int peID;
	int newEntry = 1;
	DatabaseHandler db;
	Button deletebutton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_password);
		
		db = new DatabaseHandler(this);
		name = (EditText) findViewById(R.id.nameEditText);
		username = (EditText) findViewById(R.id.usernameEditText);
		password = (EditText) findViewById(R.id.passwordEditText);
		website = (EditText) findViewById(R.id.websiteEditText);
		description = (EditText) findViewById(R.id.descriptionEditText);
		deletebutton = (Button) findViewById(R.id.deleteButton);
		
		if(getIntent().hasExtra("item_id")) {
			String itemId = getIntent().getStringExtra("item_id"); 
			Log.e("chowlb", "We have extra item_id: " + itemId);
			PasswordEntry pe = db.getPasswordEntry(Integer.parseInt(itemId));
			//deletebutton.setOnClickListener(this);
			Log.e("chowlb", "We have PE");
			newEntry = 1;
			populateView(pe);
			Log.e("chowlb", "we have populated the view");
		}else {
			newEntry = 1;
			Log.e("chowlb", "No extras sent, not populating screen");
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_password, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(newEntry == 0) {
			MenuItem meIt = (MenuItem) findViewById(R.id.saveMenuItem);
			Log.e("chowlb", "We have MenuItem");
			meIt.setIcon(android.R.drawable.ic_menu_edit);
			Log.e("chowlb", "We have changed icons");
			meIt.setTitle(R.string.edit);
			Log.e("chowlb", "we have set title");
			MenuItem delIt = (MenuItem) findViewById(R.id.deleteMenuItem);
			Log.e("chowlb", "we have delete menu item");
			delIt.setVisible(true);
			Log.e("chowlb", "we have set delete to visible");
		}
		else{
			MenuItem meIt = (MenuItem) findViewById(R.id.saveMenuItem);
			meIt.setIcon(android.R.drawable.ic_menu_save);
			meIt.setTitle(R.string.save);
			MenuItem delIt = (MenuItem) findViewById(R.id.deleteMenuItem);
			delIt.setVisible(false);
		}
		

		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.deleteMenuItem:
				deleteEntry();
				super.finish();
				break;
			case R.id.saveMenuItem:
				processEntry();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	
	public void processEntry() {
		Log.e("chowlb", "ProcessEntry Start");
		nameValue = name.getText().toString();
		usernameValue = username.getText().toString();
		passwordValue = password.getText().toString();
		websiteValue = website.getText().toString();
		descriptionValue = description.getText().toString();
		
		if(nameValue.equals("") || nameValue == null) {
			Toast.makeText(getApplicationContext(),  "Name field cannot be blank!", Toast.LENGTH_LONG).show();
		}else {
			if(usernameValue.equals("") || usernameValue == null) {
				Toast.makeText(getApplicationContext(),  "UserName field cannot be blank!", Toast.LENGTH_LONG).show();
			}else {
				if(passwordValue.equals("") || passwordValue == null) {
					Toast.makeText(getApplicationContext(),  "Password field cannot be blank!", Toast.LENGTH_LONG).show();
				}else {
					//Log.e("chowlb", "retrieved all valued from screen");
					
					PasswordEntry pe = db.getPasswordEntry(originalName);
					if(pe != null) {
						//Log.e("chowlb", "Previous entry exists for original name: " + originalName);
						pe.setName(nameValue);
						pe.setUsername(usernameValue);
						pe.setPassword(passwordValue);
						pe.setWebsite(websiteValue);
						pe.setDescription(descriptionValue);
						db.updatePasswordEntry(pe);
						//Log.e("chowlb", "Password entry existed, only updating");
					}else {
						//Log.e("chowlb", "Previous entry doesnt exist for: " + nameValue);
						db.addPasswordEntry(new PasswordEntry(nameValue, usernameValue, passwordValue, websiteValue, descriptionValue));
						//Log.e("chowlb", "Password entry did not exist, adding Entry");
					}
				    super.finish();
				}
			}
		}
		
		
	}

	public void populateView(PasswordEntry pe) {
		peID = pe.getID();
		name.setText(pe.getName());
		originalName = pe.getName();
		username.setText(pe.getUsername());
		password.setText(pe.getPassword());
		website.setText(pe.getWebsite());
		description.setText(pe.getDescription());
	}
	
	
	public void cancelAll(View view) {
		super.finish();
	}

	@Override
	public void onClick(final View v) {
		//Log.e("chowlb", "Clicked and v is " + v.getId() + " delete button id is " + deletebutton.getId());
		if(v.getId()==deletebutton.getId()) {
			new AlertDialog.Builder(this)
				.setTitle("Delete")
				.setMessage("Do you wish to delete " + db.getPasswordEntry(peID).getName() + "?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						db.deletePasswordEntry(db.getPasswordEntry(peID));
						cancelAll(v);
					}
				})
				.setNegativeButton(android.R.string.no, null).show();
			
			
			
		}
		
	}
	
	public void deleteEntry() {
		//Log.e("chowlb", "Clicked and v is " + v.getId() + " delete button id is " + deletebutton.getId());
		
			new AlertDialog.Builder(this)
				.setTitle("Delete")
				.setMessage("Do you wish to delete " + db.getPasswordEntry(peID).getName() + "?")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						db.deletePasswordEntry(db.getPasswordEntry(peID));
					}
				})
				.setNegativeButton(android.R.string.no, null).show();
			
			
			
		
		
	}
}
