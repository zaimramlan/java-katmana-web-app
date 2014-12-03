package com.katmana.model.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.katmana.model.User;

public class UserRestConfiguration extends EntityRestConfiguration<User> {
	
	User.DAO userDao;
	
	public UserRestConfiguration() {
		super();
		userDao = (User.DAO)dao;
	}

	@Override
	public void applyParams(User record, HttpServletRequest request) {
		record.setEmail(request.getParameter("email"));
	}

	@Override
	public List<User> indexRecords(HttpServletRequest request) {
		String countString = request.getParameter("count");
		int count;
		if(countString == null || countString.isEmpty()){
			count = 100; // Default count
		}else{
			count = Integer.valueOf(countString);
		}
		String offsetString = request.getParameter("offset");
		int offset;
		if(offsetString == null || offsetString.isEmpty()){
			offset = 0; // Default offset
		}else{
			offset = Integer.valueOf(offsetString);
		}
		
		return userDao.listUser(offset, count);
	}

}
