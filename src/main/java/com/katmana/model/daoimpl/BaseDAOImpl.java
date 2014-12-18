package com.katmana.model.daoimpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;

import com.katmana.model.BaseModel;

/**
 * Base class for DAO DB Implementations
 * Except a entity manager factory
 * 
 * @author asdacap
 *
 */
public class BaseDAOImpl<T extends BaseModel> implements BaseModel.DAO<T>  {

	protected EntityManager em;
	protected Class<T> entityClass;
	
	@SuppressWarnings("unchecked")
	public BaseDAOImpl(EntityManager em){
		this.em = em;
		this.entityClass = ((Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	@Override
	public boolean save(T record) {
		em.persist(record);
		em.refresh(record);
		return true;
	}
	
	@Override
	public T get(Long id) {
		T instance = em.find(entityClass, id);
		return instance;
	}

	@Override
	public boolean update(T record) {
		record = em.merge(record);
		return true;
	}

	@Override
	public boolean delete(T record) {
		record = em.merge(record);
		em.remove(record);
		return true;
	}

	@Override
	public List<T> listAll(int offset, int count) {
		return basicWhereQuery(new Hashtable<String,Object>(), offset, count);
	}
	
	@Override
	public List<T> basicWhereQuery(Map<String,Object> params,int offset, int count){
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
			Object val = params.get(par);
			try {
				Field thefield = entityClass.getDeclaredField(par);
				if(!thefield.getClass().equals(val.getClass())){
					//If not the same, attempt cast
					val = ConvertUtils.convert(val,thefield.getType());
					System.out.println("Cast attempt");
				}
			} catch (NoSuchFieldException | SecurityException | ConversionException e) {
				//Directly then
			}
			System.out.println("Field "+par+" type "+val.getClass().getCanonicalName());
			query.setParameter(par, val);
			
		}
		
		List<T> u = new ArrayList<T>(); 
		query.setMaxResults(count);
		query.setFirstResult(offset);
		u = query.getResultList();
		return u;
	}
}
