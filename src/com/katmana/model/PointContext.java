package com.katmana.model;

/**
 * A PointContext is a many-to-many relationship table
 * between Context and Point
 * 
 * @author asdacap
 *
 */
public class PointContext extends BaseModel {
	private Long point_id;
	private Long context_id;
	
	public Long getPointId() {
		return point_id;
	}
	public void setPoint_id(Long point_id) {
		this.point_id = point_id;
	}
	public Long getContextId() {
		return context_id;
	}
	public void setContextId(Long context_id) {
		this.context_id = context_id;
	}
	
	public static interface DAO extends BaseModel.DAO<PointContext>{
	}
}
