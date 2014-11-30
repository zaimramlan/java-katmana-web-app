package com.katmana.model;

/**
 * A point rating class represent rating submissions for a Point
 * A rating submission can be negative or positive.
 * 
 * @author asdacap
 *
 */
public class PointRating extends BaseModel {

	/*
	 * The point which is being liked
	 */
	private Long point_id;

	/*
	 * The one who is liking
	 */
	private Long liker_id;

	/*
	 * Like or not?
	 */
	private Boolean positive = true;

	public Long getPointId() {
		return point_id;
	}
	public void setPointId(Long point_id) {
		this.point_id = point_id;
	}
	public Long getLikerId() {
		return liker_id;
	}
	public void setLikerId(Long liker_id) {
		this.liker_id = liker_id;
	}
	public Boolean getPositive() {
		return positive;
	}
	public void setPositive(Boolean positive) {
		this.positive = positive;
	}
	
	public static interface DAO extends BaseModel.DAO<PointRating>{
	}
}
