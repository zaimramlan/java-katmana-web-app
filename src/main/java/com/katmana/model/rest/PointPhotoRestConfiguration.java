package com.katmana.model.rest;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import com.katmana.Util;
import com.katmana.model.DAOProvider;
import com.katmana.model.Point;
import com.katmana.model.PointPhoto;
import com.katmana.model.User;

public class PointPhotoRestConfiguration extends EntityRestConfiguration<PointPhoto>{

	public PointPhotoRestConfiguration(EntityManager em) {
		super(em);
	}

	@Override
	public Object getJsonableObjectRepresentation(PointPhoto record,
			HttpServletRequest request) {
		return new PointPhotoJsonableRepresentation(record, request);
	}

	@Override
	public void applyParams(PointPhoto record, HttpServletRequest request) {
		super.applyParams(record, request);
		User currentUser = Util.getCurrentUser(request);
		if(currentUser != null){
			record.setSubmitter(currentUser);
		}
		Point.DAO pdao = new DAOProvider(em).getPointDAO();
		String point_id = Util.getParameter(request, "point_id");
		if(point_id == null){
			throw new EntityRestConfiguration.RequestException("point_id is required",400);
		}
		Point point = pdao.get(Long.valueOf( point_id ));
		if(point == null){
			throw new EntityRestConfiguration.RequestException("point not found",400);
		}
		if(!point.getSubmitterId().equals(currentUser.getId())){
			throw new EntityRestConfiguration.RequestException("You do not have permission on this photo",403);
		}
		record.setPoint(point);
		try {
			Part photo = request.getPart("photo");
			if(photo != null){
				record.setContentType(photo.getContentType());
				record.setFileName(photo.getSubmittedFileName());
				record.setPhoto(IOUtils.toByteArray(photo.getInputStream()));
			}
		} catch (IOException | ServletException e) {
			e.printStackTrace();
			throw new EntityRestConfiguration.RequestException("Error",500);
		}
	}

	@Override
	public boolean allowCreate(User currentUser) {
		if(currentUser == null) return false;
		return super.allowCreate(currentUser);
	}


	@Override
	public boolean allowModify(PointPhoto record, User currentUser) {
		if(currentUser == null) return false;
		return record.getSubmitter().getId().equals(currentUser.getId());
	}
	
	public static class PointPhotoJsonableRepresentation extends EntityRestConfiguration.BaseJsonableRepresentation{

		protected String name;
		protected String description;
		protected String url;

		public PointPhotoJsonableRepresentation(PointPhoto record,HttpServletRequest request) {
			super(record);
			this.name = record.getName();
			this.description = record.getDescription();
			this.url = request.getContextPath() + "/point_photo/fetch/" + record.getId();
		}
		
	}

}
