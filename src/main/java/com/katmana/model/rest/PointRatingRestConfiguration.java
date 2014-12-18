package com.katmana.model.rest;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.katmana.Util;
import com.katmana.model.PointRating;
import com.katmana.model.User;

public class PointRatingRestConfiguration extends EntityRestConfiguration<PointRating> {
	
	PointRating.DAO pointRatingDAO;
	
	public PointRatingRestConfiguration(EntityManager em) {
		super(em);
		pointRatingDAO = (PointRating.DAO)dao;
	}

	/**
	 * Instead of getting the record by ID, 
	 * it get the record by current user id and id as point id
	 */
	@Override
	public PointRating getRecord(HttpServletRequest request) {
		User currentUser = Util.getCurrentUser(request);
		if(currentUser == null){
			throw new RequestException("You must be logged in",403);
		}
		Long point_id = getId(request);
		
		PointRating rating = pointRatingDAO.getRating(currentUser.getId(), point_id);
		return rating;
	}

	/**
	 * If logged in and 
	 */
	@Override
	public boolean allowShow(PointRating record, User currentUser) {
		if(currentUser == null)return false;
		return record.getRaterId().equals(currentUser.getId());
	}

	@Override
	public boolean allowModify(PointRating record, User currentUser) {
		return allowShow(record,currentUser);
	}

}
