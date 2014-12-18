package com.katmana.servlet;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.katmana.model.DAOProvider;
import com.katmana.model.Point;

/**
 * This is a debugging helper. It will reindex all point so that they can be searched.
 * There is no security for this. So keep that in mind.
 */
@WebServlet("/reindex_points")
public class ReindexPoints extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	EntityManager em;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Point> allPoints = DAOProvider.getInstance(em).getPointDAO().listAll(0, 1000); //Assume there will be no more than 1000 points.
		for(Point p:allPoints){
			DAOProvider.getInstance(em).getPointDAO().index(p);
		}
		response.getWriter().print(allPoints.size()+" points indexed");
	}

}
