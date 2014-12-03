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
	private Long liker_id;

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
	
	public static interface DAO extends BaseModel.DAO<SubmitterRating>{
	}
}
