package com.katmana.servlet.rest;

import javax.servlet.annotation.WebServlet;

import com.katmana.model.PointContext;
import com.katmana.model.rest.PointContextRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/point_contexts/")
public class PointContextIndexServlet extends BaseIndexServlet<PointContext,PointContextRestConfiguration> {
	private static final long serialVersionUID = 1L;
}
