<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
  <head>
    <style type="text/css">
      html, body { height: 100%; margin: 0; padding: 0;}
      #right-col { height: 100%; width:50%; float:right; margin: 0; padding: 0;}
      #map-canvas { height: 100%; width:50%; float:left; margin: 0; padding: 0;}
    </style>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js"></script>
    <script src="jquery-2.1.1.min.js"></script>
    <script type="text/javascript">
      var map, isAdding = false, active_context = null, markers = new Array();
      function initialize() {
        var mapOptions = {
          center: { lat: 3.25106, lng: 101.735866},
          zoom: 18
        };
        map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
        google.maps.event.addListener(map, 'click', function(event) {
          if(isAdding){
            var p = new Point(document.getElementById("right-col"), placeMarker(event.latLng));
            p.save();
          }
          isAdding = false
        });

        populateContext();
      }
      google.maps.event.addDomListener(window, 'load', initialize);

      function clearPoints(){
        for(var i=0; i<markers.length; i++){
          var m = markers[i];
          if(m.deleted == false){
            m.node.parentNode.removeChild(m.node);
            m.marker.setMap(null);
          }
          m.deleted = true;
        }
        markers = new Array();
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

      function Point(el, mark){
        var self = this;
        this.id = null;
        this.name = null;
        this.description = null;
        this.marker = mark;
        this.active = false;
        this.deleted = false;
        this.submitted = false;
        this.node = document.createElement("div");
        this.name_field = document.createElement("input");
        this.description_field = document.createElement("input");
        this.save_button = document.createElement("button");
        this.set_button = document.createElement("button");
        this.remove_button = document.createElement("button");

        self.node.className = "points"
        self.name_field.name = "name"
        self.description_field.name = "description"
        self.save_button.innerHTML = "Save";
        self.set_button.innerHTML = "Show";
        self.remove_button.innerHTML = "Del";

        self.node.appendChild(self.name_field);
        self.node.appendChild(self.description_field);
        self.node.appendChild(self.save_button);
        self.node.appendChild(self.set_button);
        self.node.appendChild(self.remove_button);

        this.setMarker = function(marker){
          self.marker = marker;
        }

        this.save = function(){
          var lat = self.marker.position.lat(),
              lng = self.marker.position.lng(),
              description = self.description_field.value,
              name = self.name_field.value,
              method = "PUT", url = "point/"+self.id;

          if(self.id == null){
            method = "POST";
            url = "points/";
          }

          var request = $.ajax({
            type: method,
            url: url,
            data: {name: name, description: description, latitude: lat, longitude: lng, context_id: active_context},
            dataType: "json"
          });

          request.done(function(response){
            self.id = response.id;
            self.submitted = true;
            request = $.ajax({
              type: method,
              url: "point_contexts/?point_id="+self.id+"&"+"context_id="+active_context,
              dataType: "json"
            });
          });
        }

        this.destroy = function(){
          var request = $.ajax({
            type: "DELETE",
            url: "point/"+self.id,
            dataType: "json"
          });
          request.done(function(response){
            self.submitted = true;
            self.marker.setMap(null);
            self.deleted = true;
            el.removeChild(self.node);
          })
        }

        this.showLoc = function(){
          // alert(self.marker.position);
          alert(self.id);
        }

        // set button onclick
        self.save_button.addEventListener("click", self.save, false);
        self.set_button.addEventListener("click", self.showLoc, false);
        self.remove_button.addEventListener("click", self.destroy, false);

        google.maps.event.addListener(self.marker, 'dragend', function(event) {
          self.save();
        });

        // add node to el
        el.appendChild(self.node);

        markers.push(self);
      }

      function populateMarkers(points){
        var bounds = new google.maps.LatLngBounds();
        for (var i = 0; i < points.length; i++) {
          var latLng = new google.maps.LatLng(points[i].latitude,points[i].longitude);
          var p = new Point(document.getElementById("points"), placeMarker(latLng));
          
          p.id = points[i].id;
          p.name_field.value = points[i].name;
          p.description_field.value = points[i].description;
          bounds.extend(latLng);
        };
        map.fitBounds(bounds);
      }

      function Context(el){
        var self = this;

        this.points = null;
        this.id = null;
        this.node = document.createElement("div");
        this.name_field = document.createElement("input");
        this.description_field = document.createElement("input");
        this.save_button = document.createElement("button");
        this.display_button = document.createElement("button");
        this.remove_button = document.createElement("button");

        self.node.className = "contexts"
        self.name_field.name = "name"
        self.description_field.name = "description"
        self.save_button.innerHTML = "save";
        self.display_button.innerHTML = "display";
        self.remove_button.innerHTML = "delete";

        self.node.appendChild(self.name_field);
        self.node.appendChild(self.description_field);
        self.node.appendChild(self.save_button);
        self.node.appendChild(self.display_button);
        self.node.appendChild(self.remove_button);

        this.display = function(){
          this.request = $.ajax({
            type: "GET",
            url: "points/",
            data: {context_id: self.id},
            dataType: "json"
          }).done(function(response){
            self.points = response;
            clearPoints();
            populateMarkers(self.points);
          })
        }

        this.save = function(){
          var description = self.description_field.value,
              name = self.name_field.value,
              method = "PUT", url = "context/"+self.id;

          if(self.id == null){
            method = "POST";
            url = "contexts/";
          }

          var request = $.ajax({
            type: method,
            url: url,
            data: {name: name, description: description},
            dataType: "json"
          });

          request.done(function(response){
            self.id = response.id;
            self.submitted = true;
          });
        }

        this.destroy = function(){
          var request = $.ajax({
            type: "DELETE",
            url: "context/"+self.id,
            dataType: "json"
          });
          request.done(function(response){
            el.removeChild(self.node);
          })
        }

        self.save_button.addEventListener('click', self.save, false);
        self.display_button.addEventListener('click', self.display, false);
        self.remove_button.addEventListener('click', self.destroy, false);
        self.node.addEventListener('click', function(){
          active_context = self.id;
        },false);

        el.appendChild(self.node);
      }

      function createContext(el){
        var c = new Context(el);
        c.save();
        clearPoints();
      }

      function populateContext(){
        var request = $.ajax({
          type: "GET",
          url: "contexts/",
          dataType: "json"
        }).done(function(contexts){
          for (var i = 0; i < contexts.length; i++) {
            var c = new Context(document.getElementById("contexts"));
            c.id = contexts[i].id;
            c.name_field.value = contexts[i].name;
            c.description_field.value = contexts[i].description;
          };
        })
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
      <div id="points">
        <h3>Points</h3>
        <button onclick="isAdding=true; return false">addnew</button>
      </div>
      <% } %>
      <div>
        <form action="points/">
          <input type="text" name="seacrh">
        </form>
      </div>
    </div>
  </body>
</html>