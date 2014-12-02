package com.katmana.model.daoimpl;

import javax.persistence.EntityManagerFactory;

import com.katmana.model.SubmitterRating;

public class SubmitterRatingDAOImpl extends BaseDAOImpl<SubmitterRating> implements SubmitterRating.DAO{

	public SubmitterRatingDAOImpl(EntityManagerFactory eFactory) {
		super(eFactory);
	}

}
