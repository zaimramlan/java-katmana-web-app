package com.katmana.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="point_photos")
public class PointPhoto extends BaseModel {
	
	public static final String UPLOAD_FOLDER = "point_photos";

	@NotBlank
	protected String file_name;

	@NotBlank
	protected String content_type;
	
	protected String name;
	protected String description;
	
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="point_id")
	protected Point point;

	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	protected User submitter;
	
	public String getFileName() {
		return file_name;
	}
	public void setFileName(String file_name) {
		this.file_name = file_name;
	}
	public String getContentType() {
		return content_type;
	}
	public void setContentType(String content_type) {
		this.content_type = content_type;
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
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public User getSubmitter() {
		return submitter;
	}
	public void setSubmitter(User submitter) {
		this.submitter = submitter;
	}
	
	public static interface DAO extends BaseModel.DAO<PointPhoto>{
	}

}
