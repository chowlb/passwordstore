package com.chowlb.keystore;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//TEST OF DB HANDLER
		DatabaseHandler db = new DatabaseHandler(this);
		Log.d("chowlb", "Reading all password Entries...");
		List<PasswordEntry> passEntryList = db.getAllPasswordEntry();
		//String pwcount = Integer.toString(db.getPasswordEntryCount());
		if(passEntryList.isEmpty()) {
			String log = "Password list is empty!";
			Log.d("chowlb", log);
		}else {		
			ListView peListView = (ListView) findViewById(R.id.passwordEntryList);
			ArrayAdapter<PasswordEntry> adapter = new ArrayAdapter<PasswordEntry>(this,R.layout.password_list_layout, passEntryList);
			peListView.setAdapter(adapter);
			peListView.setOnItemClickListener(new ListListener(passEntryList, this));
		}
		
		TextView count = (TextView) findViewById(R.id.countTextView);
		count.setText("");
		//count.setText(pwcount);
		//db.deleteAll();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void addEntry(View v) {
		//Intent i = new Intent(this, AddPasswordActivity.class);
		//startActivity(i);
	}
	
	public void editEntry(View view) {
		//Button clickedButton = (Button) findViewById(view.getId());
		//Intent intent = new Intent(this, ListActivity.class);
		//String message = (String) clickedButton.getText();
		//intent.putExtra("message", message);
		//startActivity(intent);
	}

}
