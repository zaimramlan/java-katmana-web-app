package com.katmana.model;

/**
 * A point represet... a point. 
 * The thing that the user(submitter) submit.
 * 
 * @author asdacap
 *
 */
public class Point extends BaseModel{

	/*
	 * The user ID who submit it.
	 */
	protected Long submitter_id;
	
	/*
	 * The coordinates. Altitude is optional.
	 */
	protected Double latitude;
	protected Double longitude;
	protected Double altitude;
	
	/*
	 * What name to put with this. Or just description?
	 * 
	 */
	protected String name;
	protected String description;
	
	/*
	 * Additional description on the location. Like, under the desk
	 * or behind the door or on the third floor.
	 */
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
	
	/**
	 * DAO for Point
	 * 
	 * @author asdacap
	 *
	 */
	public static interface DAO extends BaseModel.DAO<Point>{
	}
	
}
