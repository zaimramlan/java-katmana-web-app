package com.katmana.model;

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
	
	private DAOProvider(){}
	
	public User.DAO getUserDAO(){
		return new UserMockDAO();
	}

	public Context.DAO getContextDAO(){
		return new ContextMockDAO();
	}

	public Point.DAO getPointDAO(){
		return new PointMockDAO();
	}

	public PointContext.DAO getPointContextDAO(){
		return new PointContextMockDAO();
	}

	public PointRating.DAO getPointRatingDAO(){
		return new PointRatingMockDAO();
	}

	public SubmitterRating.DAO getSubmitterRatingDAO(){
		return new SubmitterRatingMockDAO();
	}
	
	private static DAOProvider singleton = null;
	public static DAOProvider getInstance(){
		if(singleton == null){
			singleton = new DAOProvider();
		}
		return singleton;
	}

}
