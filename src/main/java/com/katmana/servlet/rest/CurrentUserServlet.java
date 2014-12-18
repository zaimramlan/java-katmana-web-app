package com.katmana.servlet.rest;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import com.katmana.Util;
import com.katmana.model.User;
import com.katmana.model.rest.EntityRestConfiguration;
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
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User currentUser = Util.getCurrentUser(request);
		if(currentUser == null){
			response.setStatus(404, "You are not logged in");
			return;
		}
		UserRestConfiguration restConfiguration = new UserRestConfiguration((EntityManager)request.getAttribute("EntityManager"));
		try{
			User record = currentUser;
			restConfiguration.applyParams(record, request);
			
			//Validate it
			Set<ConstraintViolation<User> > violations = Util.getValidator().validate(record);
			if(violations.size() > 0){
				Map<String,Object> resp_json = new Hashtable<String,Object>();
				resp_json.put("error", "validation error");
				Set<String> errors = new TreeSet<>();
				for(ConstraintViolation<User> vio:violations){
					errors.add(vio.getMessage());
				}
				resp_json.put("validation_errors", errors);
				response.setStatus(400);
				response.getWriter().write(Util.createGson().toJson(resp_json));
				return;
			}

			restConfiguration.doUpdate(record);
			response.setStatus(202);
			response.getWriter().write(restConfiguration.serialize(record));
		}catch(EntityRestConfiguration.RequestException e){
			response.setStatus(e.getStatusCode());
			response.getWriter().write(e.getMessage());
		}
	}
	
	/**
	 * Do update
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	

}
