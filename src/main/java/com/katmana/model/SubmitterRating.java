package com.katmana.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A submitter rating class represent rating submissions for a Submitter
 * A rating submission can be negative or positive.
 * 
 * @author asdacap
 *
 */
@Entity
@Table(name="submitter_ratings")
public class SubmitterRating extends BaseModel {

	/*
	 * The one who is being liked
	 */
	private Long submitter_id;

	/*
	 * The one who is liking
	 */
	private Long rater_id;

	/*
	 * Like or not?
	 */
	private Boolean positive = true;

	public Long getSubmitterId() {
		return submitter_id;
	}
	public void setSubmitterId(Long submitter_id) {
		this.submitter_id = submitter_id;
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
	
	public static interface DAO extends BaseModel.DAO<SubmitterRating>{
		public SubmitterRating getRating(Long rater_id,Long point_id);
		public SubmitterRating.Summary getRatingSummary(Long point_id);
	}
}
