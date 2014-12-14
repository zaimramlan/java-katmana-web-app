package com.katmana.model;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * A class that represent model base. 
 * All model should implement this.
 * What will be inside? I don't know. We'll find out one day I guess.
 * 
 * @author asdacap
 */
@MappedSuperclass
public abstract class BaseModel {
	
	/*
	 * This thing is mandatory
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	/*
	 * For report keeping purposes
	 */
	protected Date created_at = new Date(Calendar.getInstance().getTimeInMillis());
	protected Date updated_at = new Date(Calendar.getInstance().getTimeInMillis());
	
	public BaseModel(){
	}
	
	public BaseModel(Long id){
		this.id = id;
	}
	
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
	public static interface DAO<T extends BaseModel>{
		public boolean save(T record); //Save the record return success flag. This method must fill the record id
		public T get(Long id); //Find the record return it or null of not found
		public List<T> listAll(int offset,int count); //Find the record return it or null of not found
		public boolean update(T record); //Update the db with record
		public boolean delete(T record); //Delete the record return true if success. False otherwise
		public List<T> basicWhereQuery(Map<String,Object> params, int offset, int count);
	}
}
