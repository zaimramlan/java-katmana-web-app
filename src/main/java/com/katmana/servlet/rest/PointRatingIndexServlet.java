package com.katmana.servlet.rest;

import javax.servlet.annotation.WebServlet;

import com.katmana.model.PointRating;
import com.katmana.model.rest.PointRatingRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/point_ratings/")
public class PointRatingIndexServlet extends BaseIndexServlet<PointRating,PointRatingRestConfiguration> {
	private static final long serialVersionUID = 1L;
}
