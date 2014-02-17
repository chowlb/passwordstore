package com.chowlb.keystore;

public class PasswordEntry {
	int _id;
	String _name;
	String _password;
	String _website;
	String _description;
	
	public PasswordEntry() {
		
	}
	
	public PasswordEntry(int id, String name, String password, String website, String description) {
		this._id = id;
		this._name = name;
		this._password = password;
		this._website = website;
		this._description = description;
	}
	
	public PasswordEntry(String name, String password, String website, String description) {
		this._name = name;
		this._password = password;
		this._website = website;
		this._description = description;
	}

	public int getID() {
		return _id;
	}

	public void setID(int _id) {
		this._id = _id;
	}

	public String getName() {
		return _name;
	}

	public void setName(String _name) {
		this._name = _name;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String _password) {
		this._password = _password;
	}

	public String getWebsite() {
		return _website;
	}

	public void setWebsite(String _website) {
		this._website = _website;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String _description) {
		this._description = _description;
	}
	
	
}
