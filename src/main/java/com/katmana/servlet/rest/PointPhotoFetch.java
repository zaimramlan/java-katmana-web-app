package com.katmana.servlet.rest;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.katmana.model.PointPhoto;
import com.katmana.model.rest.EntityRestConfiguration;
import com.katmana.model.rest.PointPhotoRestConfiguration;

/**
 * Endpoint to get the image.
 * @author asdacap
 *
 */
@WebServlet("/point_photo/fetch/*")
public class PointPhotoFetch extends BaseRestServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PointPhotoRestConfiguration restConfiguration = new PointPhotoRestConfiguration((EntityManager)request.getAttribute("EntityManager"));
		PointPhoto record = restConfiguration.getRecord(request);
		if(record == null){
			throw new EntityRestConfiguration.RequestException("Nothing to see here.",404);
		}
		if(!restConfiguration.allowShow(request)){
			throw new EntityRestConfiguration.RequestException("You do not have permission for this resource",403);
		}
		response.setContentType(record.getContentType());
		response.setStatus(200);
		IOUtils.write(record.getPhoto(), response.getOutputStream());
	}

}
