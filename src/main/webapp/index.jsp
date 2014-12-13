<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
  <head>
    <style type="text/css">
      html, body { height: 100%; margin: 0; padding: 0;}
      #right-col { height: 100%; width:50%; float:right; margin: 0; padding: 0;}
      #map-canvas { height: 100%; width:50%; float:left; margin: 0; padding: 0; line-height:normal;}
    </style>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js"></script>
    <script src="jquery-2.1.1.min.js"></script>
    <script src="point.js"></script>
    <script src="index.js"></script>
  </head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <body>
    <div id="map-canvas"></div>
    <div id="right-col">
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
      <form id="login-form" action="login" method="post">
        <input type="text" name="email">
        <input type="password" name="password">
        <input type="submit" value="login">
      </form>
      <br>
      <% } else { %>
      <form id="logout-form" action="logout">
        <input type="submit" value="logout">
      </form>
      <div id="contexts">
        <h3>Contexts</h3>
        <button onclick="createContext(this.parentNode); return false">new Context</button>
      </div>
      <% } %>
      <div>
        <form id="search-form" action="points/">
          <input type="text" name="search">
        </form>
        <script>
          $("#search-form").submit(function(event){
            event.preventDefault();
            $.get("points/?"+$("#search-form").serialize())
            .done(function(points){
              clearPoints();
              points = JSON.parse(points);
              populateMarkers({
                points:points,
                parentElement: document.getElementById("points"),
                draggable: false
              });
            })
            return false;
          })
        </script>
      </div>

      <div id="points">
        <h3>Points</h3>
        <button onclick="isAdding=true; return false">addnew</button>
      </div>
    </div>
  </body>
</html>