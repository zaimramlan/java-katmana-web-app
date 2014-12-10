package com.katmana.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

import com.katmana.model.annotation.ExcludeJson;

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

	@Field
	protected String name;
	@Field
	protected String description;
	@Field
	protected Long submitter_id;
	
	@ExcludeJson
	@ContainedIn
	@ManyToMany
	@JoinTable(name="point_contexts",
			joinColumns=@JoinColumn(name="context_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="point_id", referencedColumnName="id")
			)
	protected List<Point> points;
	

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
	public Long getSubmitterId() {
		return submitter_id;
	}
	public void setSubmitterId(Long submitter_id) {
		this.submitter_id = submitter_id;
	}
	
	public static interface DAO extends BaseModel.DAO<Context>{
		public Context getByName(String name);
	}
}
