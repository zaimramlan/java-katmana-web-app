package com.katmana.model.daoimpl;

import javax.persistence.EntityManagerFactory;

import com.katmana.model.Point;

public class PointDAOImpl extends BaseDAOImpl<Point> implements Point.DAO{

	public PointDAOImpl(EntityManagerFactory eFactory) {
		super(eFactory);
	}

}
