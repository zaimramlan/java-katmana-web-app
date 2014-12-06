package com.katmana.authentication;

import com.katmana.model.DAOProvider;
import com.katmana.model.User;
import com.katmana.Encryption;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

/**
 *
 * @author Zaid
 */
@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {

      String email = request.getParameter("email"), password = request.getParameter("password"), result;
      User user = DAOProvider.getInstance().getUserDAO().getByEmail(email);
      
      boolean validUser = (user != null), correctPass = false;
      
      if(validUser){
        correctPass = Encryption.verifyPassword(user.getEncryptedPassword(), password);
      }
      
      if(validUser && correctPass){
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        result = "<p>Username: " + email + "</p>";
        response.sendRedirect("index.jsp");
        return;
      }else{
        result = "<p>Please register.</p>";
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