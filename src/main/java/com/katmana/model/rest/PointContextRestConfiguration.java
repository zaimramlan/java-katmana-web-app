package com.katmana.model.rest;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.katmana.Util;
import com.katmana.model.Context;
import com.katmana.model.DAOProvider;
import com.katmana.model.Point;
import com.katmana.model.PointContext;
import com.katmana.model.User;

public class PointContextRestConfiguration extends EntityRestConfiguration<PointContext> {
	
	PointContext.DAO point_contextDAO;
	
	public PointContextRestConfiguration() {
		super();
		point_contextDAO = (PointContext.DAO)dao;
	}

	/**
	 * For now only allow if current user is also the submitter of context and point.
	 */
	@Override
	public boolean allowCreate(HttpServletRequest request) {
		User currentUser = Util.getCurrentUser(request);
		if(currentUser == null)return false;
		
		Point point = DAOProvider.getInstance().getPointDAO().get(Long.valueOf(request.getParameter("point_id")));
		Context context = DAOProvider.getInstance().getContextDAO().get(Long.valueOf(request.getParameter("context_id")));
		
		if(!point.getSubmitterId().equals(currentUser.getId())) return false;
		if(!context.getSubmitterId().equals(currentUser.getId())) return false;
		
		return true;
	}

	@Override
	public void doCreate(PointContext record) {
		if( point_contextDAO.getAssociation(record.getPointId(), record.getContextId()) != null ){
			throw new RequestException("The point and context has alread been associated", 400);
		}
		super.doCreate(record);
		DAOProvider.getInstance().getPointDAO().index( DAOProvider.getInstance().getPointDAO().get(record.getPointId() ));
	}
	
}
