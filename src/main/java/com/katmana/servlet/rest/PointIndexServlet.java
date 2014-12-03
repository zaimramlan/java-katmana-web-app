package com.katmana.servlet.rest;

import javax.servlet.annotation.WebServlet;

import com.katmana.model.Point;
import com.katmana.model.rest.PointRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/points/")
public class PointIndexServlet extends BaseIndexServlet<Point,PointRestConfiguration> {
	private static final long serialVersionUID = 1L;
}
