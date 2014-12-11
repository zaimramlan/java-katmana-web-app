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
@WebServlet(urlPatterns = {"/update_account"})
public class UpdateServlet extends HttpServlet {
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String accept = request.getHeader("Accept");
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
      String email, password, password_confirmation, old_password, encrypted_password, result;

      email = request.getParameter("email");
      password = request.getParameter("password");
      password_confirmation = request.getParameter("password_confirmation");
      old_password = request.getParameter("old_password");
      
      HttpSession session = request.getSession();
      User user = (User) session.getAttribute("user");

      boolean valid_user = (user != null), valid_pass, valid_old_pass, new_email;

      if(valid_user){
        new_email = email.equals(user.getEmail()) && email != null;
        valid_pass = password.equals(password_confirmation) && (password != null);
        valid_old_pass = Encryption.verifyPassword(user.getEncryptedPassword(), old_password);
        if(valid_old_pass){
            encrypted_password = Encryption.getEncryptedPassword(password);
            user.setEncryptedPassword(encrypted_password);
          }
          if(new_email){
            user.setEmail(email);
          }
          if(accept != null && accept.equals("text/json")){
            result = "{'status':'ok'}";
            response.setStatus(201);
          }else{
            result = "<p>Username: Password updated</p>";
            response.sendRedirect("index.jsp");
          }
        }
      }

      else{
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
