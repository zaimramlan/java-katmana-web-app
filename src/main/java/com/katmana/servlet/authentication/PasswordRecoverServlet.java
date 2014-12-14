package com.katmana.servlet.authentication;
import com.katmana.Encryption;
import com.katmana.Mailer;

import com.katmana.model.DAOProvider;
import com.katmana.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Zaid
 */
@WebServlet(urlPatterns = {"/recover_password"})
public class PasswordRecoverServlet extends HttpServlet {

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String accept = request.getHeader("Accept");
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {
      String email = request.getParameter("email"), result="";
      User user = DAOProvider.getInstance().getUserDAO().getByEmail(email);
      boolean valid_user = (DAOProvider.getInstance().getUserDAO().getByEmail(email) != null);

      System.out.println(email);
      System.out.println(valid_user);
      if(valid_user){
        String[] new_security = Encryption.resetPassword();
        String new_pass = new_security[0];
        String new_necrypted_password = new_security[1];
        user.setEncryptedPassword(new_necrypted_password);
        DAOProvider.getInstance().getUserDAO().update(user);
        Mailer.mail(user, new_pass);
        System.out.println("sending");
        if(accept != null && accept.equals("text/json")){
          result = "{'status':'ok'}";
          response.setStatus(201);
        }else{
          response.sendRedirect("index.jsp");
        }
      } else {
        if(accept != null && accept.equals("text/json")){
          result = "{'status':'bad'}";
          response.setStatus(404);
        }else{
          response.sendRedirect("index.jsp");
        }
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }
}
