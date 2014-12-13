var map, isAdding = false, active_context = null, markers = new Array();
function initialize() {
  var mapOptions = {
    center: { lat: 3.25106, lng: 101.735866},
    zoom: 18
  };
  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
  google.maps.event.addListener(map, 'click', function(event) {
    if(isAdding){
      var p = new Point({
        active: false,
        deleted: false,
        submitted: false,
        position: event.latLng,
        map: map,
        draggable: true,
        active_context: active_context,
        parentElement: document.getElementById("right-col")
      });
      p.save();
    }
    markers.push(p);
    isAdding = false
  });

  populateContext({parentElement: document.getElementById("contexts")});
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

function populateMarkers(param){
  var bounds = new google.maps.LatLngBounds();
  for (var i = 0; i < param.points.length; i++) {
    var point = param.points[i];
    var latLng = new google.maps.LatLng(point.latitude,point.longitude);
    var p = new Point({
      id: point.id,
      active: false,
      deleted: false,
      submitted: true,
      position: latLng,
      map: map,
      draggable: true,
      name: point.name,
      description: point.description,
      active_context: active_context,
      parentElement: param.parentElement
    });
    markers.push(p);
    bounds.extend(latLng);
  };
  map.fitBounds(bounds);
}

function Context(param){
  var self = this;

  this.points = param.points;
  this.id = param.id;
  this.submitted = param.submitted;
  this.parentElement = param.parentElement;
  this.node = document.createElement("div");
  this.name_field = document.createElement("input");
  this.description_field = document.createElement("input");
  this.save_button = document.createElement("button");
  this.display_button = document.createElement("button");
  this.remove_button = document.createElement("button");

  self.node.className = "contexts";
  self.name_field.name = "name";
  self.description_field.name = "description";
  self.name_field.value = param.name;
  self.description_field.value = param.description;
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
      populateMarkers({
        points: self.points,
        parentElement: document.getElementById("points")
      });
    })
  }

  this.save = function(){
    var description = self.description_field.value,
        name = self.name_field.value,
        method = "PUT", url = "context/"+self.id;

    if(self.submitted == false){
      method = "POST";
      url = "contexts/";
    }

    $.ajax({
      type: method,
      url: url,
      data: {name: name, description: description},
      dataType: "json"
    }).done(function(response){
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
      self.parentElement.removeChild(self.node);
    })
  }

  self.save_button.addEventListener('click', self.save, false);
  self.display_button.addEventListener('click', self.display, false);
  self.remove_button.addEventListener('click', self.destroy, false);
  self.node.addEventListener('click', function(){
    active_context = self.id;
  },false);

  if(self.submitted == false) self.save();
  self.parentElement.appendChild(self.node);
}

function createContext(el){
  var c = new Context({
    submitted: false,
    parentElement: document.getElementById("contexts")
  });
  clearPoints();
}

function populateContext(parem){
  var request = $.ajax({
    type: "GET",
    url: "contexts/",
    dataType: "json"
  }).done(function(contexts){
    for (var i = 0; i < contexts.length; i++) {
      var context = contexts[i];
      var c = new Context({
        id: context.id,
        name: context.name,
        description: context.description,
        submitted: true,
        parentElement: parem.parentElement
      });
    };
  })
}
