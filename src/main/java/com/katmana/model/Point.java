package com.katmana.model;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Latitude;
import org.hibernate.search.annotations.Longitude;
import org.hibernate.search.annotations.Spatial;

import com.katmana.model.annotation.ExcludeJson;

/**
 * A point represet... a point. 
 * The thing that the user(submitter) submit.
 * 
 * @author asdacap
 *
 */
@Entity
@Table(name="points")
@Indexed
@Spatial
public class Point extends BaseModel{

	/*
	 * The user ID who submit it.
	 */
	@Field
	protected Long submitter_id;
	
	/*
	 * The coordinates. Altitude is optional.
	 */
	@Field
	@Latitude
	protected Double latitude;
	@Field
	@Longitude
	protected Double longitude;
	@Field
	protected Double altitude;
	
	/*
	 * What name to put with this. Or just description?
	 * 
	 */
	@Field
	protected String name;
	@Field
	protected String description;
	
	@ExcludeJson
	@IndexedEmbedded
	@ManyToMany
	@JoinTable(name="point_contexts",
			joinColumns=@JoinColumn(name="point_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="context_id", referencedColumnName="id")
			)
	protected List<Context> contexts;
	
	/*
	 * Additional description on the location. Like, under the desk
	 * or behind the door or on the third floor.
	 */
	@Field
	protected String location_description;
	
	/*
	 * Getter setters
	 */
	public Long getSubmitterId() {
		return submitter_id;
	}
	public void setSubmitterId(Long submitter_id) {
		this.submitter_id = submitter_id;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getAltitude() {
		return altitude;
	}
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}
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
	public String getLocationDescription() {
		return location_description;
	}
	public void setLocationDescription(String location_description) {
		this.location_description = location_description;
	}
	public List<Context> getContexts() {
		return contexts;
	}
	public void setContexts(List<Context> contexts) {
		this.contexts = contexts;
	}
	
	/**
	 * DAO for Point
	 * 
	 * @author asdacap
	 *
	 */
	public static interface DAO extends BaseModel.DAO<Point>{
		public List<Point> searchPoint(Map<String,String> params);
		public void index(Point p);
	}
	
}
