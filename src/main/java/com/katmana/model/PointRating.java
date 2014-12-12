package com.katmana.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A point rating class represent rating submissions for a Point
 * A rating submission can be negative or positive.
 * 
 * @author asdacap
 *
 */
@Entity
@Table(name="point_ratings")
public class PointRating extends BaseModel {

	/*
	 * The point which is being liked
	 */
	private Long point_id;

	/*
	 * The one who is liking
	 */
	private Long rater_id;

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
	public Long getRaterId() {
		return rater_id;
	}
	public void setRaterId(Long rater_id) {
		this.rater_id = rater_id;
	}
	public Boolean getPositive() {
		return positive;
	}
	public void setPositive(Boolean positive) {
		this.positive = positive;
	}
	
	public static class Summary{

		private int positive = 0;
		private int negative = 0;
		
		public Summary(int positive,int negative){
			this.positive = positive;
			this.negative = negative;
		}

		public int getPositive() {
			return positive;
		}
		public int getNegative() {
			return negative;
		}
	}
	
	public static interface DAO extends BaseModel.DAO<PointRating>{
		public PointRating getRating(Long rater_id,Long point_id);
		public PointRating.Summary getRatingSummary(Long point_id);
	}
}
