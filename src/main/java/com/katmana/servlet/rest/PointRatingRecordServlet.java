package com.katmana.servlet.rest;

import java.io.IOException;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.katmana.Util;
import com.katmana.model.PointRating;
import com.katmana.model.User;
import com.katmana.model.rest.EntityRestConfiguration;
import com.katmana.model.rest.PointRatingRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/point_rating/*")
public class PointRatingRecordServlet extends BaseRecordServlet<PointRating,PointRatingRestConfiguration> {
	private static final long serialVersionUID = 1L;

	/**
	 * We have to override this because if there is no rating, it should make it here not response with 404.
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PointRatingRestConfiguration restConfiguration = getInstanceOfRestConfiguration((EntityManager)request.getAttribute("EntityManager"));
		PointRating record = restConfiguration.getRecord(request);
		if(record == null){
			/*
			 * Create it
			 */

			User currrentUser = Util.getCurrentUser(request);
			if(currrentUser == null){
				throw new EntityRestConfiguration.RequestException("You must be logged in.",403);
			}
			Long point_id = restConfiguration.getId(request);
			boolean positive = true;
			if(request.getParameter("positive") != null && request.getParameter("positive").equals("false")){
				positive = false;
			}
			PointRating pr = new PointRating();
			pr.setRaterId(currrentUser.getId());
			pr.setPointId(point_id);
			pr.setPositive(positive);

			//Validate it
			Set<ConstraintViolation<PointRating> > violations = Util.getValidator().validate(pr);
			if(violations.size() > 0){
				throw new ConstraintViolationException(violations);
			}

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

		//Validate it
		Set<ConstraintViolation<PointRating> > violations = Util.getValidator().validate(record);
		if(violations.size() > 0){
			throw new ConstraintViolationException(violations);
		}

		restConfiguration.doUpdate(record);
		response.setStatus(202);
		response.getWriter().write(restConfiguration.serialize(record));
	}
	
	
}
