package com.katmana.model.daoimpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.katmana.model.User;

public class UserDAOImpl extends BaseDAOImpl<User> implements User.DAO{

	public UserDAOImpl(EntityManagerFactory eFactory) {
		super(eFactory);
	}

	@Override
	public User getByEmail(String email) {
		EntityManager em = eFactory.createEntityManager();
		Query query = em.createQuery("select u from User u where u.email = ?");
		query.setParameter(0, email);
		User u = (User)query.getSingleResult();
		em.close();
		return u;
	}

	@Override
	public User getByName(String name) {
		EntityManager em = eFactory.createEntityManager();
		Query query = em.createQuery("select u from User u where u.name = ?");
		query.setParameter(0, name);
		User u = (User)query.getSingleResult();
		em.close();
		return u;
	}

}
