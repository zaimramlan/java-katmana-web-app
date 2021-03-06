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
                                "/javascript",
                                "/stylesheet",
                                "/media",
                                "/index.jsp",
                                "/main.html",
                                "/login.html",
                                "/login",
                                "/register.html",
                                "/register",
                                //The rest endpoint's authentication is managed by EntityRestConfiguration
                                "/point",
                                "/user",
                                "/context",
                                "/point_context",
                                "/point_rating",
                                "/point_photo",
                                "/submitter_rating"
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
    String path = uri.substring(req.getContextPath().length());
    this.context.log("Requested path::" + path);

    HttpSession session = req.getSession(false);

    if ((session == null || session.getAttribute("user") == null) && !isPublicPage(path)) {
      this.context.log("Unauthorized access request");
      res.sendRedirect(req.getContextPath()+"/main.html");
    } else {
      // pass the request along the filter chain
      chain.doFilter(request, response);
    }
  }

  private boolean isPublicPage(String path){
    for(String s:whitelist){
      if(path.startsWith(s))
        return true;
    }
    
    return false;
  }

  public void destroy() {
  }
}
