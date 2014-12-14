package com.katmana.servlet.rest;

import javax.servlet.annotation.WebServlet;

import com.katmana.model.User;
import com.katmana.model.rest.UserRestConfiguration;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user/*")
public class UserRecordServlet extends BaseRecordServlet<User,UserRestConfiguration> {
	private static final long serialVersionUID = 1L;

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setStatus(404);
  }
}
