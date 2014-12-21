function linkContains(param){
  var link = window.location.pathname;
  var result = link.indexOf(param);

  if(result === -1) { return false; }
  else { return true; }
}

function strMatches(param, parem){
  var result = param.localeCompare(parem);

  if(result === 0) return true;
  else return false;
}

function Point(param){
  var self = this;
  this.id = param.id;
  this.marker = param.marker;
  this.active = param.active;
  this.deleted = param.deleted;
  this.submitted = param.submitted;
  this.position = param.position;
  this.map = param.map;
  this.draggable = param.draggable;
  this.active_context = param.active_context;
  this.parentElement = param.parentElement;
  this.node = document.createElement("div");
  this.container_close = document.createElement("div");
  this.container_name_description = document.createElement("div");
  this.container_name = document.createElement("div");
  this.row_name = document.createElement("div");
  this.container_description = document.createElement("div");
  this.row_description = document.createElement("div");
  this.set_button = document.createElement("img"); /*not used*/
  this.save_button = document.createElement("img"); /*not used*/
  this.remove_button = document.createElement("i");
  this.name_field = document.createElement("input");
  this.description_field = document.createElement("input");

  self.node.className = "points row";
  self.container_name.className = "small-12 columns";
  self.row_name.className = "row";
  self.container_description.className = "small-12 columns";
  self.row_description.className = "row";
  self.container_name_description.className = "small-10 small-centered columns";
  self.container_close.className = "small-12 columns";
  self.name_field.name = "name";
  self.name_field.className = "name";
  self.description_field.name = "description";
  self.description_field.className = "description";
  self.name_field.value = param.name;
  self.description_field.value = param.description;
  self.set_button.src = "media/view.png"; /*not used*/
  self.save_button.src = "media/add.png"; /*not used*/
  self.remove_button.className = "fi-x";


  self.container_name.appendChild(self.name_field);
  self.row_name.appendChild(self.container_name);
  self.container_description.appendChild(self.description_field);
  self.row_description.appendChild(self.container_description);
  self.container_name_description.appendChild(self.row_name);
  self.container_name_description.appendChild(self.row_description);

  if(linkContains("main.html")) {
    self.node.appendChild(self.container_close); 
  }
  else {
    self.container_close.appendChild(self.remove_button);
    self.node.appendChild(self.container_close); 
  }
  
  self.node.appendChild(self.container_name_description); 

  this.getContentString = function(){
    return '<div id="content">'+
      '<p style="font-weight: bold; margin:0" id="firstHeading" class="firstHeading">'+param.name+'</p>'+
      '<div id="bodyContent">'+
      '<p style="margin:0">'+param.description+'</p>'+
      '</div>'+
      '</div>';
  }

  this.placeMarker = function() {
      self.marker = new google.maps.Marker({
        position: self.position,
        map: self.map,
        draggable:self.draggable,
        title: self.name_field.value
      });
      self.marker.setAnimation(google.maps.Animation.DROP);
      
      google.maps.event.addListener(self.marker, 'dragend', function(event) {
        self.save();
      });

      google.maps.event.addListener(self.marker, 'click', function() {
        self.displayInfo();
      });
  }

  this.displayInfo = function(){
    if(infowindow)
      infowindow.close();
    infowindow = new google.maps.InfoWindow({
      content: null,
      maxWidth: 300
    });
    infowindow.content = self.getContentString();
    infowindow.open(self.map, self.marker);
    self.infowindow.height(self.infowindow.height());
  }

  this.save = function(){
    var lat = self.position.lat(),
        lng = self.position.lng(),
        description = self.description_field.value,
        name = self.name_field.value,
        method = "POST", url = "points";

    if(self.id != null){
      lat = self.marker.position.lat();
      lng = self.marker.position.lng();
      method = "PUT";
      url = "point/"+self.id;
    }

    var defer = $.Deferred();

    $.ajax({
      type: method,
      url: url,
      data: {name: name, description: description, latitude: lat, longitude: lng, context_id: self.active_context},
      dataType: "json"
    }).done(function(response){
      if(!self.submitted){
        self.placeMarker();
        self.parentElement.appendChild(self.node);
        self.id = response.id;
        request = $.ajax({
          type: method,
          url: "point_contexts",
          data: {point_id: self.id, context_id: self.active_context},
          dataType: "json"
        });
        self.marker.title = self.name_field.value
        self.submitted = true;
      }
      defer.resolve(self);
    }).fail(function(){
      defer.reject();
    });

    debug("point saved!");
    return defer;
  }

  this.destroy = function(){
    $.ajax({
      type: "DELETE",
      url: "point/"+self.id,
      dataType: "json"
    }).done(function(response){
      self.marker.setMap(null);
      self.marker = null;
      self.deleted = true;
      self.parentElement.removeChild(self.node);
    })
  }

  this.showLoc = function(){
    self.map.setCenter(self.marker.position);
    self.displayInfo();
  }

  self.name_field.addEventListener("change", self.save, false);
  self.description_field.addEventListener("change", self.save, false);
  self.save_button.addEventListener("click", self.save, false);
  self.set_button.addEventListener("click", self.showLoc, false);
  self.remove_button.addEventListener("click", self.destroy, false);

  if(linkContains("main.html")) {
    self.node.addEventListener("click", self.showLoc, false);
  }

  if(self.submitted){
    self.parentElement.appendChild(self.node);
  }
}

var debug = function(msg){
  console.log(msg);
}