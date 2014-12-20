package com.katmana.servlet.rest;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.katmana.Util;
import com.katmana.model.rest.EntityRestConfiguration;

public class BaseRestServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EntityManager em = (EntityManager) request.getAttribute("EntityManager");
		try{
			super.service(request, response);
			em.getTransaction().commit();
		}catch(ConstraintViolationException ex){
			Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
			if(violations.size() > 0){
				Map<String,Object> resp_json = new Hashtable<String,Object>();
				resp_json.put("error", "validation error");
				Set<String> errors = new TreeSet<>();
				for(ConstraintViolation<?> vio:violations){
					errors.add(vio.getMessage());
				}
				resp_json.put("validation_errors", errors);
				response.setStatus(400);
				response.getWriter().write(Util.createGson().toJson(resp_json));
				return;
			}
		}catch(EntityRestConfiguration.RequestException e){
			response.setStatus(e.getStatusCode());
			response.getWriter().write(e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			throw e;
		}
	}

}
