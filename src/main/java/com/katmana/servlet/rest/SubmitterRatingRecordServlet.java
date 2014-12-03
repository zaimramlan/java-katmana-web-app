package com.katmana.servlet.rest;

import javax.servlet.annotation.WebServlet;

import com.katmana.model.SubmitterRating;
import com.katmana.model.rest.SubmitterRatingRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/submitter_ratings/*")
public class SubmitterRatingRecordServlet extends BaseRecordServlet<SubmitterRating,SubmitterRatingRestConfiguration> {
	private static final long serialVersionUID = 1L;
}
