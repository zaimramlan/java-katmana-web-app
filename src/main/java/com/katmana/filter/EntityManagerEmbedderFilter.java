package com.katmana.filter;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.katmana.Util;

/**
 * Add an EntityManager to the request attribute and commit it after the request.
 * @author asdacap
 *
 */
public class EntityManagerEmbedderFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		EntityManager em = Util.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		request.setAttribute("EntityManager", em);
		chain.doFilter(request, response);
		if(em.getTransaction().isActive()){
			em.getTransaction().commit();
		}
		em.close();
	}

	@Override
	public void destroy() {
	}

}
