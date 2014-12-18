package com.katmana.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;

import com.github.julman99.gsonfire.annotations.ExposeMethodResult;
import com.katmana.model.annotation.ExcludeJson;

/**
 * A class that model a user.
 * It fields is a one to one correspondence to the Database field.
 * 
 * @author asdacap
 */
@Entity
@Table(name="users")
public class User extends BaseModel{

	protected String name;
	protected String email;
	
	@ExcludeJson
	protected String encrypted_password;
	
	public User(){
	}
	
	public User(Long id, String name, String email, String encrypted_password){
		super(id);
		this.name = name;
		this.email = email;
		this.encrypted_password = encrypted_password;
	}
	
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
	
	/*
	@ExposeMethodResult("rating")
	public SubmitterRating.Summary getRatingSummary(){
		return DAOProvider.getInstance(em).getSubmitterRatingDAO().getRatingSummary(getId());
	}
	*/

	/**
	 * A data access object interface for the user
	 */
	public static interface DAO extends BaseModel.DAO<User>{
		public boolean addUser(String name, String email, String encrypted_password);
		public User getByEmail(String email); //Same as get, but by email
		public User getByName(String name); //Same as get, but by email
		public List<User> listAll(int offset, int count);
	}
}
