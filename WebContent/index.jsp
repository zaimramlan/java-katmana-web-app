<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>Hello World!</h1>
The time is <%= (new java.util.Date()).toLocaleString() %>

<%
  HttpSession sess = request.getSession(false);
  
  boolean logged = false;
  if(sess.getAttribute("user") != null){
    logged = true;
  }
  if(!logged) {
%>
<h2>login</h2>
<form action="login" method="post">
  <input type="text" name="email">
  <input type="password" name="password">
  <input type="submit" value="login">
</form>
<br>
<% } else { %>
<form action="logout">
  <input type="submit" value="logout">
</form>
<% } %>
</body>
</html>