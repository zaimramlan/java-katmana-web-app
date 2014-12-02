package com.katmana.model.daoimpl;

import javax.persistence.EntityManagerFactory;

import com.katmana.model.PointRating;

public class PointRatingDAOImpl extends BaseDAOImpl<PointRating> implements PointRating.DAO{

	public PointRatingDAOImpl(EntityManagerFactory eFactory) {
		super(eFactory);
	}

}
