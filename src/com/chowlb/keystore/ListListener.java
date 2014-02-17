package com.chowlb.keystore;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ListListener implements OnItemClickListener{
	List<PasswordEntry> listItems;
	
	Activity activity;
	
	public ListListener(List<PasswordEntry> aListItems, Activity anActivity) {
		listItems = aListItems;
		activity = anActivity;
	}
	
	public void onItemClick(AdapterView parent, View view, int pos, long id) {
		view.setBackgroundResource(R.color.DarkGrey);
		Intent i = new Intent(Intent.ACTION_VIEW);
		//(listItems.get(pos).getID());
		activity.startActivity(i);
	}
}
