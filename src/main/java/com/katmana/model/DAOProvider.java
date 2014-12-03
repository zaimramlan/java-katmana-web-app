package com.katmana.model;

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
	
	private EntityManagerFactory mFactory;
	private User.DAO userDAO;
	private Context.DAO contextDAO;
	private Point.DAO pointDAO;
	private PointContext.DAO pointContextDAO;
	private PointRating.DAO pointRatingDAO;
	private SubmitterRating.DAO submitterRatingDAO;

	private DAOProvider(){
		mFactory = Persistence.createEntityManagerFactory("KatManaDB");
		userDAO = new UserDAOImpl(mFactory);
		contextDAO = new ContextDAOImpl(mFactory);
		pointContextDAO = new PointContextDAOImpl(mFactory);
		pointDAO = new PointDAOImpl(mFactory);
		pointRatingDAO = new PointRatingDAOImpl(mFactory);
		submitterRatingDAO = new SubmitterRatingDAOImpl(mFactory);
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
		return null;
	}
	
	private static DAOProvider singleton = null;
	public synchronized static DAOProvider getInstance(){
		if(singleton == null){
			singleton = new DAOProvider();
		}
		return singleton;
	}

}
