package com.katmana.model.rest;

import javax.servlet.http.HttpServletRequest;

import com.katmana.Util;
import com.katmana.model.SubmitterRating;
import com.katmana.model.User;

public class SubmitterRatingRestConfiguration extends EntityRestConfiguration<SubmitterRating> {
	
	SubmitterRating.DAO submitterRatingDAO;
	
	public SubmitterRatingRestConfiguration() {
		super();
		submitterRatingDAO = (SubmitterRating.DAO)dao;
	}

	/**
	 * Instead of getting the record by ID, 
	 * it get the record by current user id and id as submitter id
	 */
	@Override
	public SubmitterRating getRecord(HttpServletRequest request) {
		User currentUser = Util.getCurrentUser(request);
		if(currentUser == null){
			throw new RequestException("You must be logged in",403);
		}
		Long submitter_id = getId(request);
		
		SubmitterRating rating = submitterRatingDAO.getRating(currentUser.getId(), submitter_id);
		return rating;
	}

	/**
	 * If logged in and 
	 */
	@Override
	public boolean allowShow(SubmitterRating record, User currentUser) {
		if(currentUser == null)return false;
		return record.getRaterId().equals(currentUser.getId());
	}

	@Override
	public boolean allowModify(SubmitterRating record, User currentUser) {
		return allowShow(record,currentUser);
	}

}
