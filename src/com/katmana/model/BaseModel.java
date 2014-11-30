package com.katmana.model;

import java.sql.Date;

/**
 * A class that represent model base. 
 * All model should implement this.
 * What will be inside? I don't know. We'll find out one day I guess.
 * 
 * @author asdacap
 */
public abstract class BaseModel {
	
	/*
	 * This thing is mandatory
	 */
	protected Long id;
	/*
	 * For report keeping purposes
	 */
	protected Date created_at;
	protected Date updated_at;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreatedAt() {
		return created_at;
	}
	public void setCreatedAt(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdatedAt() {
		return updated_at;
	}
	public void setUpdatedAt(Date updated_at) {
		this.updated_at = updated_at;
	}
	

	/**
	 * A base interface for DAO (Data Access Object)
	 * All model DAO should extends from this. 
	 *
	 * @author asdacap
	 */
	public static interface DAO<T>{
		public T save(T record); //Save the record return a new record with id filled
		public T get(Long id); //Find the record return it or null of not found
		public boolean delete(Long id); //Delete the record return true if success. False otherwise
	}
}
