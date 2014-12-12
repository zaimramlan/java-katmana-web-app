package com.katmana.servlet.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.katmana.Util;
import com.katmana.model.Point;
import com.katmana.model.PointRating;
import com.katmana.model.User;
import com.katmana.model.rest.EntityRestConfiguration;
import com.katmana.model.rest.PointRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/point/*")
public class PointRecordServlet extends BaseRecordServlet<Point,PointRestConfiguration> {
	private static final long serialVersionUID = 1L;

}
