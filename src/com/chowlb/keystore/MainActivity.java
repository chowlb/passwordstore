package com.chowlb.keystore;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity {
	
	DatabaseHandler db;
	TextView emptyList;
	LinearLayout layout;
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
		
		
		if(passEntryList.isEmpty()) {
			String log = "Password list is empty!";
			//Log.e("chowlb", "Hiding list view");
			
			layout.removeView(peListView);
			//Log.e("chowlb", "List view hidden");
			LayoutParams lparams = new LayoutParams(
					   LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			emptyList.setLayoutParams(lparams);
			adView.setLayoutParams(lparams);
			//Log.e("chowlb", "Set layout params");
			emptyList.setText("No entries found. Press MENU and 'Add Entry'");
			emptyList.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			emptyList.setPadding(0, 10, 0, 0);
			layout.addView(adView, 0);
			layout.addView(emptyList, 1);
			//Log.e("chowlb", "Added emptyList to LinearLayout");
			
			//Log.e("chowlb", log);
		}else {		
			layout.removeView(emptyList);
			LayoutParams lparams = new LayoutParams(
					   LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			peListView.setLayoutParams(lparams);
			layout.addView(peListView,0);
			adView.setLayoutParams(lparams);
			layout.addView(adView, 1);
			peListView.setVisibility(View.VISIBLE);
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
		layout = (LinearLayout) findViewById(R.id.LinearLayout1);
		peListView = (ListView) findViewById(R.id.passwordEntryList);
		//Log.e("chowlb", "CREATE");
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
