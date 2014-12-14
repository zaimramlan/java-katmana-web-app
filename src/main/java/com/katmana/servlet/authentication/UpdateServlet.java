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
      String email, name, password, password_confirmation, old_password, encrypted_password, result="";

      email = request.getParameter("email");
      name = request.getParameter("name");
      password = request.getParameter("password");
      password_confirmation = request.getParameter("password_confirmation");
      old_password = request.getParameter("old_password");
      
      HttpSession session = request.getSession();
      User current_user = (User) session.getAttribute("user");
      User user = DAOProvider.getInstance().getUserDAO().getByEmail(current_user.getEmail());

      boolean valid_user = (current_user != null), valid_new_pass, authentic, new_email;

      if(valid_user){
        new_email = (DAOProvider.getInstance().getUserDAO().getByEmail(email) == null) && (email.length() > 0);
        valid_new_pass = password.equals(password_confirmation) && (password.length() > 0);
        authentic = Encryption.verifyPassword(user.getEncryptedPassword(), old_password);

        if(authentic){

          if(valid_new_pass){
            encrypted_password = Encryption.getEncryptedPassword(password);
            user.setEncryptedPassword(encrypted_password);
          }
          if(new_email){
            user.setEmail(email);
          }
          if(name.length() > 0){
            user.setName(name);
          }
          DAOProvider.getInstance().getUserDAO().update(user);
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
      response.getWriter().println(result);
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
