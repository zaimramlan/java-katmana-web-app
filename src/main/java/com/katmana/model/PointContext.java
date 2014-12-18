package com.katmana.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * A PointContext is a many-to-many relationship table
 * between Context and Point
 * 
 * @author asdacap
 *
 */
@Entity
@Table(name="point_contexts")
public class PointContext extends BaseModel {
	@NotNull(message="point id must not be null")
	private Long point_id;
	@NotNull(message="context id must not be null")
	private Long context_id;
	
	public Long getPointId() {
		return point_id;
	}
	public void setPointId(Long point_id) {
		this.point_id = point_id;
	}
	public Long getContextId() {
		return context_id;
	}
	public void setContextId(Long context_id) {
		this.context_id = context_id;
	}
	
	public static interface DAO extends BaseModel.DAO<PointContext>{
		public PointContext getAssociation(Long point_id,Long context_id);
	}
}
