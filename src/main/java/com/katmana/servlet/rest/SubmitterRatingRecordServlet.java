	package com.katmana.servlet.rest;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.katmana.Util;
import com.katmana.model.SubmitterRating;
import com.katmana.model.User;
import com.katmana.model.rest.EntityRestConfiguration;
import com.katmana.model.rest.SubmitterRatingRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/submitter_rating/*")
public class SubmitterRatingRecordServlet extends BaseRecordServlet<SubmitterRating,SubmitterRatingRestConfiguration> {
	private static final long serialVersionUID = 1L;

	/**
	 * We have to override this because if there is no rating, it should make it here not response with 404.
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SubmitterRatingRestConfiguration restConfiguration = getInstanceOfRestConfiguration((EntityManager)request.getAttribute("EntityManager"));
		try{
			SubmitterRating record = restConfiguration.getRecord(request);
			if(record == null){
				/*
				 * Create it
				 */

				User currrentUser = Util.getCurrentUser(request);
				if(currrentUser == null){
					throw new EntityRestConfiguration.RequestException("You must be logged in.",403);
				}
				Long submitter_id = restConfiguration.getId(request);
				boolean positive = true;
				if(request.getParameter("positive") != null && request.getParameter("positive").equals("false")){
					positive = false;
				}
				SubmitterRating pr = new SubmitterRating();
				pr.setRaterId(currrentUser.getId());
				pr.setSubmitterId(submitter_id);
				pr.setPositive(positive);
				restConfiguration.doCreate(pr);
				response.setStatus(201);
				response.getWriter().write(restConfiguration.serialize(record));
				
			}
			if(!restConfiguration.allowUpdate(request)){
				response.setStatus(403);
				response.getWriter().write("You do not have permission for this resource");
				return;
			}
			restConfiguration.applyParams(record, request);
			restConfiguration.doUpdate(record);
			response.setStatus(202);
			response.getWriter().write(restConfiguration.serialize(record));
		}catch(EntityRestConfiguration.RequestException e){
			response.setStatus(e.getStatusCode());
			response.getWriter().write(e.getMessage());
		}
	}
}
