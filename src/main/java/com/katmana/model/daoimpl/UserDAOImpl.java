package com.katmana.model.daoimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.katmana.model.User;

public class UserDAOImpl extends BaseDAOImpl<User> implements User.DAO{

	public UserDAOImpl(EntityManagerFactory eFactory) {
		super(eFactory);
	}

	@Override
	public User getByEmail(String email) {
		EntityManager em = eFactory.createEntityManager();
		Query query = em.createQuery("select u from User u where u.email = :email");
		query.setParameter("email", email);
		User u = null; 
		try{
			u = (User)query.getSingleResult();
		}catch(NoResultException e){}
		em.close();
		return u;
	}

	@Override
	public User getByName(String name) {
		EntityManager em = eFactory.createEntityManager();
		Query query = em.createQuery("select u from User u where u.name = :name");
		query.setParameter("name", name);
		User u = null; 
		try{
			u = (User)query.getSingleResult();
		}catch(NoResultException e){}
		em.close();
		return u;
	}

	@Override
	public boolean addUser(String name, String email, String encrypted_password) {
		User user = new User(null,name,email,encrypted_password);
		return save(user);
	}

	@Override
	public List<User> listUser(int offset, int count) {
		EntityManager em = eFactory.createEntityManager();
		Query query = em.createQuery("select u from User u");
		List<User> u = new ArrayList<User>(); 
		query.setMaxResults(count);
		query.setFirstResult(offset);
		u = query.getResultList();
		em.close();
		return u;
	}

}
