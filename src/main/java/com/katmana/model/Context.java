package com.katmana.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A context is basically a way to group points.
 * A context may be a place or an event or a category of points or 
 * just about anything that can group points
 * A point may be associated with more than one context.
 * 
 * @author asdacap
 *
 */
@Entity
@Table(name="contexts")
public class Context extends BaseModel {

	private String name;
	private String description;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public static interface DAO extends BaseModel.DAO<Context>{
		public Context getByName(String name);
	}
}
