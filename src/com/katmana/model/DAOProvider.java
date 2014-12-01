package com.katmana.model;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.katmana.model.daoimpl.UserDAOImpl;
import com.katmana.model.mockdao.ContextMockDAO;
import com.katmana.model.mockdao.PointContextMockDAO;
import com.katmana.model.mockdao.PointMockDAO;
import com.katmana.model.mockdao.PointRatingMockDAO;
import com.katmana.model.mockdao.SubmitterRatingMockDAO;
import com.katmana.model.mockdao.UserMockDAO;

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
		contextDAO = new ContextMockDAO();
		pointContextDAO = new PointContextMockDAO();
		pointDAO = new PointMockDAO();
		pointRatingDAO = new PointRatingMockDAO();
		submitterRatingDAO = new SubmitterRatingMockDAO();
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
	
	private static DAOProvider singleton = null;
	public synchronized static DAOProvider getInstance(){
		if(singleton == null){
			singleton = new DAOProvider();
		}
		return singleton;
	}

}
