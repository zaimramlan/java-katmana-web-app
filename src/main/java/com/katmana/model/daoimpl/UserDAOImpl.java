package com.katmana.model.daoimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.katmana.model.User;

public class UserDAOImpl extends BaseDAOImpl<User> implements User.DAO{

	public UserDAOImpl(EntityManager em) {
		super(em);
	}

	@Override
	public User getByEmail(String email) {
		Query query = em.createQuery("select u from User u where u.email = :email");
		query.setParameter("email", email);
		User u = null; 
		try{
			u = (User)query.getSingleResult();
		}catch(NoResultException e){}
		return u;
	}

	@Override
	public User getByName(String name) {
		Query query = em.createQuery("select u from User u where u.name = :name");
		query.setParameter("name", name);
		User u = null; 
		try{
			u = (User)query.getSingleResult();
		}catch(NoResultException e){}
		return u;
	}

	@Override
	public boolean addUser(String name, String email, String encrypted_password) {
		User user = new User(null,name,email,encrypted_password);
		return save(user);
	}

}
