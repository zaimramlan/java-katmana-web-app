<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <style type="text/css">
      html, body { height: 100%; margin: 0; padding: 0;}
      #right-col { height: 100%; width:50%; float:right; margin: 0; padding: 0;}
      #map-canvas { height: 100%; width:50%; float:left; margin: 0; padding: 0;}
    </style>
    <script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js">
    </script>
    <script type="text/javascript">
      var map, isAdding = false, points = Array();
      function initialize() {
        var mapOptions = {
          center: { lat: 3.25106, lng: 101.735866},
          zoom: 18
        };
        map = new google.maps.Map(document.getElementById('map-canvas'),
            mapOptions);
        google.maps.event.addListener(map, 'click', function(event) {
          if(isAdding){
            var p = new Point(document.getElementById("right-col"));
            p.setMarker(placeMarker(event.latLng))
            points.push(p);
          }
          isAdding = false
        });
      }

      function placeMarker(location) {
        var marker = new google.maps.Marker({
            position: location,
            map: map,
            draggable:true
        });
        marker.setAnimation(google.maps.Animation.DROP);
        return marker;
      }
      google.maps.event.addDomListener(window, 'load', initialize);

      function Point(el){
        var self = this;
        this.name = null;
        this.description = null;
        this.marker = null;
        this.active = false;
        this.deleted = false;
        this.node = document.createElement("div");
        this.name_field = document.createElement("input");
        this.description_field = document.createElement("input");
        this.set_button = document.createElement("button");
        this.remove_button = document.createElement("button");

        self.node.className = "points"
        self.name_field.name = "name"
        self.description_field.name = "description"
        self.set_button.innerHTML = "Show";
        self.remove_button.innerHTML = "Del";

        self.node.appendChild(self.name_field);
        self.node.appendChild(self.description_field);
        self.node.appendChild(self.set_button);
        self.node.appendChild(self.remove_button);

        this.setMarker = function(marker){
          self.marker = marker;
        }

        this.destroy = function(){
          self.marker.setMap(null);
          self.deleted = true;
          el.removeChild(self.node);
        }

        this.showLoc = function(){
          alert(self.marker.position);
        }

        // bounce onmouseenter
        self.node.addEventListener("mouseenter", function(){
          self.marker.setAnimation(google.maps.Animation.BOUNCE);
        }, false);

        // stop animation onmouseleave
        self.node.addEventListener("mouseleave", function(){
          self.marker.setAnimation(null);
        }, false);

        // set button onclick
        self.set_button.addEventListener("click", self.showLoc, false);
        self.remove_button.addEventListener("click", self.destroy, false);

        // add node to el
        el.appendChild(self.node);
      }

      function showAll(){
        for(i=0; i<points.length; i++){
          if(points[i].deleted != true)
            alert(points[i].marker.position);
        }
      }

      function submit(){
        xmlhttp=new XMLHttpRequest();
        xmlhttp.open("POST","/points",true);
        xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");

        for(i=0; i<points.length; i++){
          if(points[i].deleted != true){
            var lat = points[i].marker.position.lat();
            var lng = points[i].marker.position.lng();

            xmlhttp.send("latitude="+lat+"&longitude="+lng);
          }
        }
      }
    </script>
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
      <button onclick="submit(); return false">submit</button>
      <button onclick="showAll(); return false">showall</button>
      <button onclick="isAdding=true; return false">addnew</button>
    </div>
  </body>
</html>