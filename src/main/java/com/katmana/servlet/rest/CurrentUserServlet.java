package com.katmana.servlet.rest;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.katmana.Util;
import com.katmana.model.User;
import com.katmana.model.rest.UserRestConfiguration;

@WebServlet("/user/me")
public class CurrentUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User currentUser = Util.getCurrentUser(req);
		if(currentUser == null){
			resp.setStatus(404, "You are not logged in");
			return;
		}
		EntityManager em = (EntityManager)req.getAttribute("EntityManager");
		resp.getWriter().write(new UserRestConfiguration(em).serialize(currentUser));
	}
	
	

}
