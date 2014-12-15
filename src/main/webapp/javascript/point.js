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
  this.container_name = document.createElement("div");
  this.container_desc = document.createElement("div");
  this.container_menu = document.createElement("div");  
  this.name_field = document.createElement("input");
  this.description_field = document.createElement("input");
  this.save_button = document.createElement("img");
  this.set_button = document.createElement("img");
  this.remove_button = document.createElement("img");

  self.node.className = "points"
  self.container_name.className = "small-12 columns";
  self.container_desc.className = "small-12 columns";
  self.container_menu.className = "small-12 columns";  
  self.name_field.name = "name"
  self.description_field.name = "description"
  self.name_field.value = param.name;
  self.description_field.value = param.description;
  self.save_button.src = "media/add.png";
  self.set_button.src = "media/view.png";
  self.remove_button.src = "media/rem.png";

  // self.node.appendChild(self.name_field);
  // self.node.appendChild(self.description_field);
  // self.node.appendChild(self.save_button);
  // self.node.appendChild(self.set_button);
  // self.node.appendChild(self.remove_button);

  self.container_name.appendChild(self.name_field);
  self.container_desc.appendChild(self.description_field);
  self.container_menu.appendChild(self.save_button);
  self.container_menu.appendChild(self.set_button);
  self.container_menu.appendChild(self.remove_button);
  self.node.appendChild(self.container_name);
  self.node.appendChild(self.container_desc);
  self.node.appendChild(self.container_menu);  

  this.getContentString = function(){
    return '<div id="content">'+
      '<p style="font-weight: bold; margin:0" id="firstHeading" class="firstHeading">'+self.name_field.value+'</p>'+
      '<div id="bodyContent">'+
      '<p style="margin:0">'+self.description_field.value+'</p>'+
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

  // set button onclick
  self.save_button.addEventListener("click", self.save, false);
  self.set_button.addEventListener("click", self.showLoc, false);
  self.remove_button.addEventListener("click", self.destroy, false);

  if(self.submitted){
    self.parentElement.appendChild(self.node);
  }
}
