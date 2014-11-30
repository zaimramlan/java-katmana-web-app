package com.katmana.model;


/**
 * A class that model a user.
 * It fields is a one to one correspondence to the Database field.
 * 
 * @author asdacap
 */
public class User extends BaseModel{

	protected String name;
	protected String email;
	protected String encrypted_password;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEncryptedPassword() {
		return encrypted_password;
	}
	public void setEncryptedPassword(String encrypted_password) {
		this.encrypted_password = encrypted_password;
	}
	
	/**
	 * A data access object interface for the user
	 */
	public static interface DAO extends BaseModel.DAO<User>{
		public User getByEmail(String email); //Same as get, but by email
		public User getByName(String email); //Same as get, but by email
	}
}
