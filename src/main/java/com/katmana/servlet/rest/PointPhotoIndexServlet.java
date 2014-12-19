package com.katmana.servlet.rest;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.katmana.Util;
import com.katmana.model.PointPhoto;
import com.katmana.model.daoimpl.PointPhotoRestConfiguration;
import com.katmana.model.rest.EntityRestConfiguration;

public class PointPhotoIndexServlet extends BaseIndexServlet<PointPhoto, PointPhotoRestConfiguration>{

	/**
	 * Do create.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	PointPhotoRestConfiguration restConfiguration = getInstanceOfRestConfiguration((EntityManager)request.getAttribute("EntityManager"));
    	if(!restConfiguration.allowCreate(request)){
			throw new EntityRestConfiguration.RequestException("You do not have permission for this resource",403);
    	}

    	PointPhoto record;
    	try {
    		record = recordClass.newInstance();
    	} catch (InstantiationException | IllegalAccessException e) {
    		e.printStackTrace();
    		response.sendError(500, "Record instantiation exception.");
    		return;
    	}
    	
    	restConfiguration.applyParams(record, request);
    	Part photo = request.getPart("photo");
    	record.setContentType(photo.getContentType());
    	record.setFileName(photo.getSubmittedFileName());
    	
    	String savePath = request.getServletContext().getRealPath("") + File.separator + PointPhoto.UPLOAD_FOLDER;

    	//Validate it
    	Set<ConstraintViolation<PointPhoto> > violations = Util.getValidator().validate(record);
    	if(violations.size() > 0){
    		throw new ConstraintViolationException(violations);
    	}

    	restConfiguration.doCreate(record);

    	photo.write(savePath + File.separator + record.getId() + File.separator + record.getFileName());

    	response.setStatus(201);
    	response.getWriter().write(restConfiguration.serialize(record));
    }

}
