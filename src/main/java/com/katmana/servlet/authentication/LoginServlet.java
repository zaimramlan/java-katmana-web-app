package com.katmana.servlet.authentication;

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.katmana.Encryption;
import com.katmana.model.DAOProvider;
import com.katmana.model.User;

/**
 *
 * @author Zaid
 */
@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
	
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
	EntityManager em = (EntityManager)request.getAttribute("EntityManager");

    String accept = request.getHeader("Accept");
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {

      String email = request.getParameter("email"), password = request.getParameter("password"), result;
      User user = DAOProvider.getInstance(em).getUserDAO().getByEmail(email);
      
      boolean validUser = (user != null), correctPass = false;
      
      if(validUser){
        correctPass = Encryption.verifyPassword(user.getEncryptedPassword(), password);
      }
      
      if(validUser && correctPass){
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        if(accept != null && accept.equals("text/json")){
          result = "{'status':'ok'}";
          response.setStatus(201);
        }else{
          result = "<p>Username: " + email + "</p>";
          response.sendRedirect("home.html");
        }
      }else{
          if(accept != null && accept.equals("text/json")){

          result = "{'status':'wrong username and password'}";
          response.setStatus(403);
        }else{
          result = "<p>Please register.</p>";
        }
      }
      out.println(result);
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  public String getServletInfo() {
    return "Short description";
  }
  
}
