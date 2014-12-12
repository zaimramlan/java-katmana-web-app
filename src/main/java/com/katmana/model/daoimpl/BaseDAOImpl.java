package com.katmana.model.daoimpl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.katmana.model.BaseModel;

/**
 * Base class for DAO DB Implementations
 * Except a entity manager factory
 * 
 * @author asdacap
 *
 */
public class BaseDAOImpl<T extends BaseModel> implements BaseModel.DAO<T>  {

	protected EntityManagerFactory eFactory;
	protected Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public BaseDAOImpl(EntityManagerFactory eFactory){
		this.eFactory = eFactory;
		this.entityClass = ((Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	@Override
	public boolean save(T record) {
		EntityManager em = eFactory.createEntityManager();
		em.getTransaction().begin();
		em.persist(record);
		em.refresh(record);
		em.getTransaction().commit();
		em.close();
		return true;
	}
	
	@Override
	public T get(Long id) {
		EntityManager em = eFactory.createEntityManager();
		T instance = em.find(entityClass, id);
		em.close();
		return instance;
	}

	@Override
	public boolean update(T record) {
		EntityManager em = eFactory.createEntityManager();
		em.getTransaction().begin();
		record = em.merge(record);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	@Override
	public boolean delete(T record) {
		EntityManager em = eFactory.createEntityManager();
		em.getTransaction().begin();
		record = em.merge(record);
		em.remove(record);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	@Override
	public List<T> listAll(int offset, int count) {
		return basicWhereQuery(new Hashtable<String,Object>(), offset, count);
	}
	
	@Override
	public List<T> basicWhereQuery(Map<String,Object> params,int offset, int count){
		EntityManager em = eFactory.createEntityManager();
		String whereQuery = "";
		boolean first = true;
		
		//Build the where query
		for(String par:params.keySet()){
			if(!first){
				whereQuery += " AND ";
			}else{
				first = false;
			}
			whereQuery+= "u."+par+" = :"+par;
		}
		//If not empty, add 'WHERE'
		if(!whereQuery.isEmpty()){
			whereQuery = "WHERE "+whereQuery;
		}
		
		Query query = em.createQuery("select u from "+entityClass.getName()+" u "+whereQuery);
		
		//Assign parameter value
		for(String par:params.keySet()){
			query.setParameter(par, params.get(par));
		}
		
		List<T> u = new ArrayList<T>(); 
		query.setMaxResults(count);
		query.setFirstResult(offset);
		u = query.getResultList();
		em.close();
		return u;
	}
}
