package com.katmana.servlet.rest;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.katmana.model.DAOProvider;
import com.katmana.model.PointContext;
import com.katmana.model.rest.EntityRestConfiguration;
import com.katmana.model.rest.PointContextRestConfiguration;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/point_contexts")
public class PointContextIndexServlet extends BaseIndexServlet<PointContext,PointContextRestConfiguration> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * In point_contexts, no such thing as get list of association.
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(404);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		EntityManager em = (EntityManager)req.getAttribute("EntityManager");
		PointContextRestConfiguration restConfiguration = getInstanceOfRestConfiguration(em);
		if(req.getParameter("point_id") == null || req.getParameter("context_id") == null){
			resp.setStatus(400);
			resp.getWriter().write("Parameter point_id and context_id is required");
			return;
		}
		PointContext association = DAOProvider.getInstance(em).getPointContextDAO().getAssociation(Long.valueOf(req.getParameter("point_id")), Long.valueOf(req.getParameter("context_id")));
		if(association == null){
			resp.setStatus(404);
			return;
		}
		DAOProvider.getInstance(em).getPointDAO().index( DAOProvider.getInstance(em).getPointDAO().get(association.getPointId() ));
		restConfiguration.doDestroy(association);
		resp.setStatus(200);
		resp.getWriter().write(restConfiguration.serialize(association,req));
	}
	
	
}
