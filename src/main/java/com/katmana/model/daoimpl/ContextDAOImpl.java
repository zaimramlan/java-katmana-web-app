package com.katmana.model.daoimpl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.katmana.model.Context;
import com.katmana.model.DAOProvider;
import com.katmana.model.Point;

public class ContextDAOImpl extends BaseDAOImpl<Context> implements Context.DAO{

	public ContextDAOImpl(EntityManager em) {
		super(em);
	}

	@Override
	public Context getByName(String name) {
		Query query = em.createQuery("select u from Context u where u.name = :name");
		query.setParameter("name", name);
		Context u = null; 
		try{
			u = (Context)query.getSingleResult();
		}catch(NoResultException e){}
		return u;
	}

	@Override
	public boolean delete(Context record) {
		Point.DAO pointdao = new DAOProvider(em).getPointDAO();
		for(Point p:record.getPoints()){
			if(p.getContexts().size()==1){
				pointdao.delete(p);
			}
		}
		return super.delete(record);
	}

}
