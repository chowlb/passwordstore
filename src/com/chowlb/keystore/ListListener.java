package com.chowlb.keystore;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ListListener implements OnItemClickListener{
	List<PasswordEntry> listItems;
	
	Activity activity;
	
	public ListListener(List<PasswordEntry> aListItems, Activity anActivity) {
		listItems = aListItems;
		activity = anActivity;
	}
	
	public void onItemClick(AdapterView parent, View view, int pos, long id) {
		view.setBackgroundResource(R.color.DarkGrey);
		Intent intent = new Intent(activity, AddPasswordActivity.class);
		PasswordEntry pe = listItems.get(pos);
		intent.putExtra("item_id", String.valueOf(pe.getID()));
		activity.startActivity(intent);
	}
}
