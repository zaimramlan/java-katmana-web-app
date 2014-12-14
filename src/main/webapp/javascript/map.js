var map, isAdding = false, active_context = null, markers = new Array();
google.maps.event.addDomListener(window, 'load', initialize);

function initialize() {
  var mapOptions = {
    center: { lat: 3.25106, lng: 101.735866},
    zoom: 18
  };
  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
  google.maps.event.addListener(map, 'click', function(event) {
    if(isAdding){
      var p = new Point(document.getElementById("points"), placeMarker(event.latLng));
      p.save();
    }
    isAdding = false
  });

  populateContext();
}

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
  this.save_button = document.createElement("img");
  this.set_button = document.createElement("img");
  this.remove_button = document.createElement("img");

  /*
  Changes for UI
  */
  this.container_name = document.createElement("div");
  this.container_desc = document.createElement("div");
  this.container_menu = document.createElement("div");

  self.node.className = "points"
  self.name_field.name = "name"
  self.description_field.name = "description"
  self.save_button.src = "media/add.png";
  self.set_button.src = "media/view.png";
  self.remove_button.src = "media/rem.png";

  /*
  Changes for UI
  */
  self.container_name.className = "small-12 columns";
  self.container_desc.className = "small-12 columns";
  self.container_menu.className = "small-12 columns";

  // self.node.appendChild(self.name_field);
  // self.node.appendChild(self.description_field);
  // self.node.appendChild(self.save_button);
  // self.node.appendChild(self.set_button);
  // self.node.appendChild(self.remove_button);

  /*
  Changes for UI
  */
  self.container_name.appendChild(self.name_field);
  self.container_desc.appendChild(self.description_field);
  self.container_menu.appendChild(self.save_button);
  self.container_menu.appendChild(self.set_button);
  self.container_menu.appendChild(self.remove_button);
  self.node.appendChild(self.container_name);
  self.node.appendChild(self.container_desc);
  self.node.appendChild(self.container_menu);

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
  this.save_button = document.createElement("img");
  this.display_button = document.createElement("img");
  this.remove_button = document.createElement("img");

  /*
  Changes for UI
  */
  this.container_name = document.createElement("div");
  this.container_desc = document.createElement("div");
  this.container_menu = document.createElement("div");

  self.node.className = "contexts"
  self.name_field.name = "name"
  self.description_field.name = "description"
  self.save_button.src = "media/add.png";
  self.display_button.src = "media/view.png";
  self.remove_button.src = "media/rem.png";

  /*
  Changes for UI
  */
  self.container_name.className = "small-12 columns";
  self.container_desc.className = "small-12 columns";
  self.container_menu.className = "small-12 columns";

  self.node.appendChild(self.name_field);
  self.node.appendChild(self.description_field);
  self.node.appendChild(self.save_button);
  self.node.appendChild(self.display_button);
  self.node.appendChild(self.remove_button);

  /*
  Changes for UI
  */
  self.container_name.appendChild(self.name_field);
  self.container_desc.appendChild(self.description_field);
  self.container_menu.appendChild(self.save_button);
  self.container_menu.appendChild(self.display_button);
  self.container_menu.appendChild(self.remove_button);
  self.node.appendChild(self.container_name);
  self.node.appendChild(self.container_desc);
  self.node.appendChild(self.container_menu);

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
      var c = new Context(document.getElementById("context"));
      c.id = contexts[i].id;
      c.name_field.value = contexts[i].name;
      c.description_field.value = contexts[i].description;
    };
  })
}