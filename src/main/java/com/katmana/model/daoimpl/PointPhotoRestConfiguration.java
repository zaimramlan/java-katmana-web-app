package com.katmana.model.daoimpl;

import javax.persistence.EntityManager;

import com.katmana.model.PointPhoto;
import com.katmana.model.rest.EntityRestConfiguration;

public class PointPhotoRestConfiguration extends EntityRestConfiguration<PointPhoto>{

	public PointPhotoRestConfiguration(EntityManager em) {
		super(em);
	}

}
