package com.katmana.model.rest;

import javax.servlet.http.HttpServletRequest;

import com.katmana.Util;
import com.katmana.model.Context;
import com.katmana.model.User;

public class ContextRestConfiguration extends EntityRestConfiguration<Context> {

	@Override
	public void applyParams(Context record, HttpServletRequest request) {
		super.applyParams(record, request);
		User currentUser = Util.getCurrentUser(request);
		if(currentUser != null){
			record.setSubmitterId(currentUser.getId());
		}
	}

	@Override
	public boolean allowCreate(User currentUser) {
		if(currentUser == null) return false;
		return super.allowCreate(currentUser);
	}


	@Override
	public boolean allowModify(Context record, User currentUser) {
		if(currentUser == null) return false;
		return record.getSubmitterId().equals(currentUser.getId());
	}

}
