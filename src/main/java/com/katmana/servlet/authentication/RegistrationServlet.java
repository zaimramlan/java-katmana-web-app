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

import com.katmana.Encryption;
import com.katmana.model.DAOProvider;
import com.katmana.model.User;

/**
 *
 * @author Zaid
 */
@WebServlet(name = "RegistrationServlet", urlPatterns = {"/register"})
public class RegistrationServlet extends HttpServlet {

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
	EntityManager em = (EntityManager)request.getAttribute("EntityManager");
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {

      String name, email, password, password_confirmation, encrypted_password;

      name = request.getParameter("name");
      email = request.getParameter("email");
      password = request.getParameter("password");
      password_confirmation = request.getParameter("password_confirmation");
      
      User user = DAOProvider.getInstance(em).getUserDAO().getByEmail(email);
      boolean new_user = (user == null), valid_pass = password.equals(password_confirmation);

      if(new_user && valid_pass){
        encrypted_password = Encryption.getEncryptedPassword(password);
        DAOProvider.getInstance(em).getUserDAO().addUser(name, email, encrypted_password);
        out.println("<h1>Successfully Registered</h1>");
        response.sendRedirect("index.jsp");
        return;
      }
      else if (!new_user){
        out.println("<h1>Email already registered</h1>");
        return;
      }
      else {
        out.println("<h1>Password mismatch</h1>");
        return;
      }
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
