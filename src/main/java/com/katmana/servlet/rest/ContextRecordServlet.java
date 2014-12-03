package com.katmana.servlet.rest;

import javax.servlet.annotation.WebServlet;

import com.katmana.model.Context;
import com.katmana.model.rest.ContextRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/contexts/*")
public class ContextRecordServlet extends BaseRecordServlet<Context,ContextRestConfiguration> {
	private static final long serialVersionUID = 1L;
}
