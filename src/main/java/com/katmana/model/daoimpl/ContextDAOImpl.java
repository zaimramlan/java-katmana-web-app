package com.katmana.model.daoimpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.katmana.model.Context;

public class ContextDAOImpl extends BaseDAOImpl<Context> implements Context.DAO{

	public ContextDAOImpl(EntityManagerFactory eFactory) {
		super(eFactory);
	}

	@Override
	public Context getByName(String name) {
		EntityManager em = eFactory.createEntityManager();
		Query query = em.createQuery("select u from Context u where u.name = :name");
		query.setParameter("name", name);
		Context u = null; 
		try{
			u = (Context)query.getSingleResult();
		}catch(NoResultException e){}
		em.close();
		return u;
	}

}
