package com.katmana.servlet.rest;

import javax.servlet.annotation.WebServlet;

import com.katmana.model.User;
import com.katmana.model.rest.UserRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user/*")
public class UserRecordServlet extends BaseRecordServlet<User,UserRestConfiguration> {
	private static final long serialVersionUID = 1L;

  @override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setStatus(404);
  }
}
