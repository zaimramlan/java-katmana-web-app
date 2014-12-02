/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katmana.authentication;

import static com.katmana.authentication.Encryption.getEncryption;
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
@WebServlet(name = "Registration", urlPatterns = {"/register"})
public class Registration extends HttpServlet {

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    try (PrintWriter out = response.getWriter()) {

      String name, email, password, password_confirmation, encrypted_password;

      name = request.getParameter("name");
      email = request.getParameter("email");
      password = request.getParameter("password");
      password_confirmation = request.getParameter("password_confirmation");
      
      User user = DAOProvider.getInstance().getUserDAO().getByEmail(email);
      boolean new_user = (user == null), valid_pass = password.equals(password_confirmation);

      if(new_user && valid_pass){
        encrypted_password = getEncryption(password);
        DAOProvider.getInstance().getUserDAO().addUser(name, email, encrypted_password);
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
