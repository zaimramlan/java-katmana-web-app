package com.katmana.model.daoimpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import com.katmana.model.PointContext;

public class PointContextDAOImpl extends BaseDAOImpl<PointContext> implements PointContext.DAO{

	public PointContextDAOImpl(EntityManager em) {
		super(em);
	}

	@Override
	public PointContext getAssociation(Long point_id, Long context_id) {
		PointContext result = null;
		try{
			result = (PointContext) em.createQuery("SELECT pc FROM PointContext pc WHERE pc.point_id = :point_id AND pc.context_id = :context_id")
			.setParameter("point_id", point_id).setParameter("context_id", context_id).getSingleResult();
		}catch(NoResultException nre){
		}
		return result;
	}

}
