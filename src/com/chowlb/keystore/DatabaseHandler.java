package com.chowlb.keystore;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
	//All Static Variables
	//DatabaseVersion
	private static final int DATABASE_VERSION = 1;
	//Database Name
	private static final String DATABASE_NAME = "passwordStore";
	//Table Name
	private static final String TABLE_PASS_STORE = "store";
	
	//Columns
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_WEBSITE = "website";
	private static final String KEY_DESCRIPTION = "description";
	
	private static byte[] key = {
        0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
	};//"thisIsASecretKey";
	final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_PASS_STORE = "CREATE TABLE " + TABLE_PASS_STORE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_NAME + " TEXT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_WEBSITE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE_PASS_STORE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASS_STORE);
		onCreate(db);
	}
	
	// Adding new contact
	public void addPasswordEntry(PasswordEntry passEntry) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, passEntry.getName());
		values.put(KEY_USERNAME, passEntry.getUsername());
		values.put(KEY_PASSWORD, encryptPassword(passEntry.getPassword()));
		values.put(KEY_WEBSITE, passEntry.getWebsite());
		values.put(KEY_DESCRIPTION, passEntry.getDescription());
		
		db.insert(TABLE_PASS_STORE, null, values);
		db.close();
	}
	 
	// Getting single contact
	public PasswordEntry getPasswordEntry(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_PASS_STORE, new String[] {KEY_ID, KEY_NAME, KEY_USERNAME, KEY_PASSWORD, KEY_WEBSITE, KEY_DESCRIPTION}, KEY_ID + "=?",
				new String[] {String.valueOf(id)}, null, null, null, null);
		if(cursor != null)
			cursor.moveToFirst();
			
		PasswordEntry passEntry = new PasswordEntry(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), decryptPassword(cursor.getString(3)), cursor.getString(4), cursor.getString(5));
		
		return passEntry;
	}
	
	public PasswordEntry getPasswordEntry(String name) {
		SQLiteDatabase db = this.getReadableDatabase();
		//Log.e("chowlb", "Call to getpasswordentry with name: " + name);
		Cursor cursor = db.query(TABLE_PASS_STORE, new String[] {KEY_ID, KEY_NAME, KEY_USERNAME, KEY_PASSWORD, KEY_WEBSITE, KEY_DESCRIPTION}, KEY_NAME + "=?",
				new String[] {String.valueOf(name)}, null, null, null, null);
		if((cursor != null) && (cursor.getCount()> 0)) {
			//Log.e("chowlb", "Cursor is not null");
			cursor.moveToFirst();
		
			PasswordEntry passEntry = new PasswordEntry(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), decryptPassword(cursor.getString(3)), cursor.getString(4), cursor.getString(5));
			return passEntry;
		}
		else {
			//Log.e("chowlb", "Cursor is null, returning null");
			return null;
		}
	}
	
	 
	// Getting All Contacts
	public List<PasswordEntry> getAllPasswordEntry() {
		List<PasswordEntry> passEntryList = new ArrayList<PasswordEntry>();
		String selectQuery = "SELECT * FROM " + TABLE_PASS_STORE;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst()) {
			do {
				PasswordEntry passEntry = new PasswordEntry();
				passEntry.setID(Integer.parseInt(cursor.getString(0)));
				passEntry.setName(cursor.getString(1));
				passEntry.setUsername(cursor.getString(2));
				passEntry.setPassword(decryptPassword(cursor.getString(3)));
				passEntry.setWebsite(cursor.getString(4));
				passEntry.setDescription(cursor.getString(5));
				
				
				passEntryList.add(passEntry);
			}while(cursor.moveToNext());
		}
		return passEntryList;
	}
	 
	// Getting contacts Count
	public int getPasswordEntryCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PASS_STORE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
	}

	// Updating single contact
	public int updatePasswordEntry(PasswordEntry passEntry) {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();

	    values.put(KEY_NAME, passEntry.getName());
	    values.put(KEY_USERNAME, passEntry.getUsername());
	    values.put(KEY_PASSWORD, encryptPassword(passEntry.getPassword()));
	    values.put(KEY_WEBSITE, passEntry.getWebsite());
	    values.put(KEY_DESCRIPTION, passEntry.getDescription());
	 
	    // updating row
	    return db.update(TABLE_PASS_STORE, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(passEntry.getID()) });
	}
	 
	// Deleting single contact
	public void deletePasswordEntry(PasswordEntry passEntry) {
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_PASS_STORE, KEY_ID + " = ?",
	            new String[] { String.valueOf(passEntry.getID()) });
	    db.close();
	}
	
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PASS_STORE, null, null);
	}

	public String encryptPassword(String plainPassword) {
		
		try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return new String (Base64.encode(cipher.doFinal(plainPassword.getBytes()), Base64.DEFAULT));
        }
        catch (Exception e)
        {
            Log.e("chowlb", "Error while encrypting : " + e);
        }
        return null;
	}
	
	public String decryptPassword(String encPassword) {
		
		 try
	        {
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	            cipher.init(Cipher.DECRYPT_MODE, secretKey);
	            return new String(cipher.doFinal(Base64.decode(encPassword, Base64.DEFAULT)));
	            
	        }
	        catch (Exception e)
	        {
	            Log.e("chowlb", "Error while decrypting : " + e);

	        }
	        return null;
	}
	
}
