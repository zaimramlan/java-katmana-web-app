package com.katmana.model.daoimpl;

import javax.persistence.EntityManagerFactory;

import com.katmana.model.PointContext;

public class PointContextDAOImpl extends BaseDAOImpl<PointContext> implements PointContext.DAO{

	public PointContextDAOImpl(EntityManagerFactory eFactory) {
		super(eFactory);
	}

}
