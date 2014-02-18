package com.chowlb.keystore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

//public class AddPasswordActivity extends Activity implements OnClickListener{
public class AddPasswordActivity extends Activity{
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
	
	private AdView adView;
	
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
		
		if(getIntent().hasExtra("item_id")) {
			String itemId = getIntent().getStringExtra("item_id"); 
			PasswordEntry pe = db.getPasswordEntry(Integer.parseInt(itemId));
			
			newEntry = 0;
			populateView(pe);
		}else {
			newEntry = 1;
		}
		
		adView = new AdView(this);
		adView.setAdUnitId("ca-app-pub-8858215261311943/5415676714");
		adView.setAdSize(AdSize.BANNER);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.addPasswordLayout);
		RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
				   LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, adView.getId());
		
		adView.setLayoutParams(lparams);
		layout.addView(adView);
		
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(Build.VERSION.SDK_INT >= 11) {
			selectMenu(menu);
		}
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(Build.VERSION.SDK_INT < 11) {
			selectMenu(menu);
		}
		return true;
	}
	
	public void selectMenu(Menu menu) {
		menu.clear();
		MenuInflater inflater = getMenuInflater();
		if(newEntry == 0) {
			inflater.inflate(R.menu.add_password_edit, menu);
		}
		else{
			inflater.inflate(R.menu.add_password, menu);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.deleteMenuItem:
				deleteEntry();
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
		//Log.e("chowlb", "ProcessEntry Start");
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
	
	public void deleteEntry() {
		new AlertDialog.Builder(this)
			.setTitle("Delete")
			.setMessage("Do you wish to delete " + db.getPasswordEntry(peID).getName() + "?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					db.deletePasswordEntry(db.getPasswordEntry(peID));
					finish();
				}
			})
			.setNegativeButton(android.R.string.no, null).show();
	}
	
	public void togglePassword(View v) {
		int inputType = password.getInputType();
		//Log.e("chowlb", "InputType = " + inputType);
		if(inputType == 1) {
			//Log.e("chowlb", "Hiding password");
			password.setInputType(129);
		}else {
			//Log.e("chowlb", "Showing password");
			password.setInputType(1);
		}
	}
}
