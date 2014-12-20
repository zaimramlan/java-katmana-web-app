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
			String positive_str = Util.getParameter(request, "positive");
			boolean positive = true;
			if(positive_str != null && positive_str.equals("false")){
				positive = false;
			}
			SubmitterRating pr = new SubmitterRating();
			pr.setRaterId(currrentUser.getId());
			pr.setSubmitterId(submitter_id);
			pr.setPositive(positive);

			//Validate it
			Set<ConstraintViolation<SubmitterRating> > violations = Util.getValidator().validate(pr);
			if(violations.size() > 0){
				throw new ConstraintViolationException(violations);
			}

			restConfiguration.doCreate(pr);
			response.setStatus(201);
			response.getWriter().write(restConfiguration.serialize(record,request));

		}
		if(!restConfiguration.allowUpdate(request)){
			response.setStatus(403);
			response.getWriter().write("You do not have permission for this resource");
			return;
		}
		restConfiguration.applyParams(record, request);

		//Validate it
		Set<ConstraintViolation<SubmitterRating> > violations = Util.getValidator().validate(record);
		if(violations.size() > 0){
			throw new ConstraintViolationException(violations);
		}

		restConfiguration.doUpdate(record);
		response.setStatus(202);
		response.getWriter().write(restConfiguration.serialize(record,request));
	}
}
