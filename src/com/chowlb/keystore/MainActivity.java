package com.chowlb.keystore;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//TEST OF DB HANDLER
		DatabaseHandler db = new DatabaseHandler(this);
		
		db.addPasswordEntry(new PasswordEntry("chowlb", "password8", "chowlb.com", "password for main site"));
		db.addPasswordEntry(new PasswordEntry("chowlbie", "password9", "boa.com", "password for main site"));
		db.addPasswordEntry(new PasswordEntry("chowlb", "password0", "test.com", "password for main site"));
		db.addPasswordEntry(new PasswordEntry("chowlb", "password14", "twcable.com", "password for main site"));
		
		Log.d("chowlb", "Reading all password Entries...");
		List<PasswordEntry> passEntryList = db.getAllPasswordEntry();
		
		for(PasswordEntry pe : passEntryList) {
			String log = "ID:" + pe.getID()
					+ ", Name: " + pe.getName()					
					+ ", Password: " + pe.getPassword()
					+ ", Webiste: " + pe.getWebsite()
					+ ", Desc: " + pe.getDescription();
			Log.d("chowlb", log);
					
		}
		db.deleteAll();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
