package com.katmana.model.rest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.katmana.model.DAOProvider;
import com.katmana.model.SubmitterRating;
import com.katmana.model.User;

public class UserRestConfiguration extends EntityRestConfiguration<User> {

	public UserRestConfiguration(EntityManager em) {
		super(em);
	}

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

	@Override
	public List<String> getWritableRecordProperties() {
		List<String> resp = super.getWritableRecordProperties();
		resp.remove("encrypted_password");
		return resp;
	}

	@Override
	public List<String> getQueryableRecordProperties() {
		List<String> resp = super.getWritableRecordProperties();
		resp.remove("encrypted_password");
		return resp;
	}
	
	@Override
	public Object getJsonableObjectRepresentation(User record,HttpServletRequest request){
		return new JsonableObjectRepresentation(record,em);
	}

	@Override
	public Object getListJsonableObjectRepresentation(User record,HttpServletRequest request){
		return new JsonableObjectRepresentation(record,em);
	}

	/**
	 * Its this object that will be jsonified.
	 * @author asdacap
	 */
	public static class JsonableObjectRepresentation extends EntityRestConfiguration.BaseJsonableRepresentation{
		protected String name;
		protected String email;
		protected SubmitterRating.Summary rating;
	
		public JsonableObjectRepresentation(User p,EntityManager em){
			super(p);
			name = p.getName();
			email = p.getEmail();
			
			DAOProvider daoprov = new DAOProvider(em);
			
			rating = daoprov.getSubmitterRatingDAO().getRatingSummary(id);
		}
	}
	
}
