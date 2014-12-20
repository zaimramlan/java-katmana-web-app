package com.katmana.model.daoimpl;

import javax.persistence.EntityManager;

import com.katmana.model.BaseModel;
import com.katmana.model.PointPhoto;

public class PointPhotoDAOImpl extends BaseDAOImpl<PointPhoto> implements PointPhoto.DAO{

	public PointPhotoDAOImpl(EntityManager em) {
		super(em);
	}

}
