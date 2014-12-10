package com.katmana.model.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.katmana.Util;
import com.katmana.model.Point;
import com.katmana.model.User;

public class PointRestConfiguration extends EntityRestConfiguration<Point> {
	
	Point.DAO pointDAO;
	
	public PointRestConfiguration() {
		super();
		pointDAO = (Point.DAO)dao;
	}
	
	

	@Override
	public List<Point> indexRecords(HttpServletRequest request) {
		
		if(request.getParameter("search") != null && !request.getParameter("search").isEmpty()){
			return pointDAO.searchPoint(request.getParameter("search"));
		}
		
		return super.indexRecords(request);
	}

	

	@Override
	public boolean allowCreate(User currentUser) {
		if(currentUser == null) return false;
		return super.allowCreate(currentUser);
	}


	@Override
	public boolean allowModify(Point record, User currentUser) {
		if(currentUser == null) return false;
		return record.getSubmitterId().equals(currentUser.getId());
	}



	@Override
	public void applyParams(Point record, HttpServletRequest request) {
		super.applyParams(record, request);
		User currentUser = Util.getCurrentUser(request);
		if(currentUser != null){
			record.setSubmitterId(currentUser.getId());
		}
	}

}
