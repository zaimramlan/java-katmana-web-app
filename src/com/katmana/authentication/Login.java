package com.katmana.authentication;

import com.katmana.model.DAOProvider;
import com.katmana.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Zaid
 */
@WebServlet(urlPatterns = {"/login"})
public class Login extends HttpServlet {
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {

      String email = request.getParameter("email"), password = request.getParameter("password"), result;
      User user = DAOProvider.getInstance().getUserDAO().getByEmail(email);
      
      boolean validUser = user != null, correctPass = false;
      
      if(validUser)
        correctPass = user.getEncryptedPassword().equals(Encryption.getEncryption(password));
      
      if(validUser && correctPass){
        HttpSession session = request.getSession();
        session.setAttribute("user", email);
        result = "<p>Username: " + email + "</p>";
        response.sendRedirect("index.jsp");
        return;
      }else{
        result = "<p>Please register.</p>";
      }

      out.println("<!DOCTYPE html>");
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servlet Login</title>");      
      out.println("</head>");
      out.println("<body>");
      out.println("<h1>Information</h1>");
      out.println(result);
      out.println("</body>");
      out.println("</html>");
    }
  }

  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>

}
