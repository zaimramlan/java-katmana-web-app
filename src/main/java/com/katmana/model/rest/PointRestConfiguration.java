package com.katmana.model.rest;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
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
		
		Map<String,String> params = new Hashtable<String,String>();
		
		Enumeration<String> names = request.getParameterNames();
		for(;names.hasMoreElements();){
			String parName = names.nextElement();
			params.put(parName, request.getParameter(parName));
		}
		
		return pointDAO.searchPoint(params);
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
