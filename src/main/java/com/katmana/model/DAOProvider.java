package com.katmana.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.katmana.model.daoimpl.ContextDAOImpl;
import com.katmana.model.daoimpl.PointContextDAOImpl;
import com.katmana.model.daoimpl.PointDAOImpl;
import com.katmana.model.daoimpl.PointRatingDAOImpl;
import com.katmana.model.daoimpl.SubmitterRatingDAOImpl;
import com.katmana.model.daoimpl.UserDAOImpl;

/**
 * A class that provide DAO.
 * We should only get DAO from this class so that we can change DAO implementation as necessary
 * 
 * @author asdacap
 *
 */
public class DAOProvider {
	
	private EntityManager em;
	private User.DAO userDAO;
	private Context.DAO contextDAO;
	private Point.DAO pointDAO;
	private PointContext.DAO pointContextDAO;
	private PointRating.DAO pointRatingDAO;
	private SubmitterRating.DAO submitterRatingDAO;

	public DAOProvider(EntityManager em){
		this.em = em;
		userDAO = new UserDAOImpl(em);
		contextDAO = new ContextDAOImpl(em);
		pointContextDAO = new PointContextDAOImpl(em);
		pointDAO = new PointDAOImpl(em);
		pointRatingDAO = new PointRatingDAOImpl(em);
		submitterRatingDAO = new SubmitterRatingDAOImpl(em);
	}
	
	public User.DAO getUserDAO(){
		return userDAO;
	}

	public Context.DAO getContextDAO(){
		return contextDAO;
	}

	public Point.DAO getPointDAO(){
		return pointDAO;
	}

	public PointContext.DAO getPointContextDAO(){
		return pointContextDAO;
	}

	public PointRating.DAO getPointRatingDAO(){
		return pointRatingDAO;
	}

	public SubmitterRating.DAO getSubmitterRatingDAO(){
		return submitterRatingDAO;
	}
	
	public BaseModel.DAO<?> getDaoByType(Class<?> recordType){
		if(recordType.equals(User.class)){
			return getUserDAO();
		}
		if(recordType.equals(Context.class)){
			return getContextDAO();
		}
		if(recordType.equals(Point.class)){
			return getPointDAO();
		}
		if(recordType.equals(PointContext.class)){
			return getPointContextDAO();
		}
		if(recordType.equals(PointRating.class)){
			return getPointRatingDAO();
		}
		if(recordType.equals(SubmitterRating.class)){
			return getSubmitterRatingDAO();
		}
		return null;
	}
	
	public static DAOProvider getInstance(EntityManager em){
		return new DAOProvider(em);
	}
}
