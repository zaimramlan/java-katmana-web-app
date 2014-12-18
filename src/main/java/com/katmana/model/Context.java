package com.katmana.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.validator.constraints.NotBlank;

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
	@NotBlank(message="Name must not be blank")
	protected String name;

	@Field
	@NotBlank(message="Description must not be blank")
	protected String description;

	@Field
	@NotNull(message="Submitter id must not be null")
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
	public List<Point> getPoints(){
		return points;
	}
	
	public static interface DAO extends BaseModel.DAO<Context>{
		public Context getByName(String name);
	}
}
