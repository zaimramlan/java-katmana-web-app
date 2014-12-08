package com.katmana.model.rest;

import javax.servlet.http.HttpServletRequest;

import com.katmana.model.User;

public class UserRestConfiguration extends EntityRestConfiguration<User> {

	@Override
	public boolean allowShow(User record, User currentUser) {
		/*
		 * Same rule
		 */
		return allowModify(record, currentUser);
	}

	@Override
	public boolean allowModify(User record, User currentUser) {
		if(currentUser == null){
			/*
			 * Need to be logged in.
			 */
			return false;
		}
		if(record.getId() != currentUser.getId()){
			/*
			 * Not the same person.
			 */
			return false;
		}else{
			/*
			 * Sure. Why not?
			 */
			return true;
		}
	}

	@Override
	public boolean allowResource(User currentUser) {
		/*
		 * Default is, NO.
		 */
		return false;
	}
	
}
