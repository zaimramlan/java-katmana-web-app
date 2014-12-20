package com.katmana.model.rest;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.katmana.Util;
import com.katmana.model.Context;
import com.katmana.model.DAOProvider;
import com.katmana.model.Point;
import com.katmana.model.PointPhoto;
import com.katmana.model.PointRating;
import com.katmana.model.User;

public class PointRestConfiguration extends EntityRestConfiguration<Point> {
	
	Point.DAO pointDAO;
	
	public PointRestConfiguration(EntityManager em) {
		super(em);
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

	@Override
	public Object getJsonableObjectRepresentation(Point record,HttpServletRequest request){
		return new JsonableObjectRepresentation(record,em,request);
	}

	@Override
	public Object getListJsonableObjectRepresentation(Point record,HttpServletRequest request){
		return new ListJsonableObjectRepresentation(record,em);
	}

	/**
	 * Its this object that will be jsonified.
	 * @author asdacap
	 */
	public static class JsonableObjectRepresentation extends EntityRestConfiguration.BaseJsonableRepresentation{
		protected Long submitter_id;
		protected Double latitude;
		protected Double longitude;
		protected Double altitude;
		protected String name;
		protected String description;
		protected String location_description;
		protected List<Object> contexts;
		protected PointRating.Summary rating;
		protected List<Object> photos;
	
		public JsonableObjectRepresentation(Point p,EntityManager em,HttpServletRequest request){
			super(p);
			submitter_id = p.getSubmitterId();
			latitude = p.getLatitude();
			longitude = p.getLongitude();
			altitude = p.getAltitude();
			name = p.getName();
			description = p.getDescription();
			location_description = p.getLocationDescription();
			
			DAOProvider daoprov = new DAOProvider(em);
			
			ContextRestConfiguration crestconf = new ContextRestConfiguration(em);
			
			contexts = new ArrayList<>();
			for(Context c:p.getContexts()){
				contexts.add(crestconf.getListJsonableObjectRepresentation(c,request));
			}
			
			rating = daoprov.getPointRatingDAO().getRatingSummary(id);
			
			PointPhotoRestConfiguration prestconf = new PointPhotoRestConfiguration(em);
			
			photos = new ArrayList<>();
			for(PointPhoto pp:p.getPhotos()){
				photos.add(prestconf.getListJsonableObjectRepresentation(pp, request));
			}
		}
	}
	
	/**
	 * Its this object that will be jsonified too. But this one is for list.
	 * @author asdacap
	 */
	public static class ListJsonableObjectRepresentation extends EntityRestConfiguration.BaseJsonableRepresentation{
		protected Long submitter_id;
		protected Double latitude;
		protected Double longitude;
		protected Double altitude;
		protected String name;
		protected String description;
		protected String location_description;
		protected PointRating.Summary rating;
	
		public ListJsonableObjectRepresentation(Point p,EntityManager em){
			super(p);
			submitter_id = p.getSubmitterId();
			latitude = p.getLatitude();
			longitude = p.getLongitude();
			altitude = p.getAltitude();
			name = p.getName();
			description = p.getDescription();
			location_description = p.getLocationDescription();
			
			DAOProvider daoprov = new DAOProvider(em);
			
			rating = daoprov.getPointRatingDAO().getRatingSummary(id);
		}
	}

}
