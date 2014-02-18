package com.chowlb.keystore;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity {
	
	DatabaseHandler db;
	TextView emptyList;
	RelativeLayout layout;
	List<PasswordEntry> passEntryList;
	ListView peListView;
	private AdView adView;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//Log.e("chowlb", "Activity Result");
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		//Log.e("chowlb", "Restart");
		refreshMainView();
		super.onRestart();
		//Log.e("chowlb", "End restart");
	}

	protected void refreshMainView() {
		layout.removeView(emptyList);
		layout.removeView(peListView);
		passEntryList = db.getAllPasswordEntry();
		adView = new AdView(this);
		adView.setAdUnitId("ca-app-pub-8858215261311943/5415676714");
		adView.setAdSize(AdSize.BANNER);
		
		//Check if password list is empty
		if(passEntryList.isEmpty()) {
			
			layout.removeView(peListView);
			
			RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
					   LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, adView.getId());
			
			RelativeLayout.LayoutParams toplparams = new RelativeLayout.LayoutParams(
					   LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			toplparams.addRule(RelativeLayout.ALIGN_PARENT_TOP, emptyList.getId());
			
			emptyList.setLayoutParams(toplparams);
			adView.setLayoutParams(lparams);
			
			emptyList.setText("No entries found. Press MENU and 'Add Entry'");
			emptyList.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			emptyList.setPadding(0, 10, 0, 0);
			
			layout.addView(emptyList);
			layout.addView(adView);
			
		}else {		
			layout.removeView(emptyList);
			
			RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
					   LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, adView.getId());
			
			RelativeLayout.LayoutParams toplparams = new RelativeLayout.LayoutParams(
					   LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			toplparams.addRule(RelativeLayout.ALIGN_PARENT_TOP, emptyList.getId());
			
			peListView.setLayoutParams(toplparams);
			layout.addView(peListView);
			
			adView.setLayoutParams(lparams);
			layout.addView(adView);
			
			ArrayAdapter<PasswordEntry> adapter = new ArrayAdapter<PasswordEntry>(this,R.layout.password_list_layout, passEntryList);
			peListView.setAdapter(adapter);
			peListView.setOnItemClickListener(new ListListener(passEntryList, this));
		}
		 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new DatabaseHandler(this);
		emptyList = new TextView(this);
		layout = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		peListView = (ListView) findViewById(R.id.passwordEntryList);
		
		//Creating the actual layout (determines if we have a list or an empty list;
		refreshMainView();
		
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		//Log.e("chowlb", "END CREATE");
		//db.deleteAll();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.addEntryMenuItem:
				Intent i = new Intent(this, AddPasswordActivity.class);
				this.startActivity(i);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}
	

}
