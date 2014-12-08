package com.katmana.servlet.authentication;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author http://www.journaldev.com/
 */

@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {
  private ServletContext context;
  private String[] whitelist = {
                                "index.jsp",
                                "login",
                                "registration.html",
                                "register"
                              };

  @Override
  public void init(FilterConfig fConfig) throws ServletException {
    this.context = fConfig.getServletContext();
    this.context.log("AuthenticationFilter initialized");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    String uri = req.getRequestURI();
    this.context.log("Requested Resource::" + uri);

    HttpSession session = req.getSession(false);

    if ((session == null || session.getAttribute("user") == null) && !isPublicPage(uri)) {
      this.context.log("Unauthorized access request");
      res.sendRedirect(req.getContextPath()+"/index.jsp");
    } else {
      // pass the request along the filter chain
      chain.doFilter(request, response);
    }
  }

  private boolean isPublicPage(String uri){
    for(String s:whitelist){
      if(uri.endsWith(s))
        return true;
    }
    
    return false;
  }

  public void destroy() {
  }
}
