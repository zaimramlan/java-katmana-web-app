package com.katmana.servlet.rest;

import javax.servlet.annotation.WebServlet;

import com.katmana.model.User;
import com.katmana.model.rest.UserRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/users/*")
public class UserRecordServlet extends BaseRecordServlet<User,UserRestConfiguration> {
	private static final long serialVersionUID = 1L;
}
