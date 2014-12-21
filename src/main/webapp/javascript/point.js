function Point(param){
  var self = this;
  this.id = param.id;
  this.marker = param.marker;
  this.active = param.active;
  this.deleted = param.deleted;
  this.submitted = param.submitted;
  this.position = param.position;
  this.map = param.map;
  this.point_obj = param.point;
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
  self.description_field.name = "description";
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

  if(self.point_obj != undefined && (user == null || user.id != self.point_obj.submitter_id) ){
    //No permission for this point
  }else{
    self.container_close.appendChild(self.remove_button);
  }
  self.node.appendChild(self.container_close); 
  self.node.appendChild(self.container_name_description); 

  this.getContentString = function(){
    return get_template('point-content-string')(self.point_obj);
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
    // self.infowindow.height(self.infowindow.height());
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
        self.point_obj = response;
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
      self.point_obj = response;
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

  this.select = function(){
    self.showLoc();
    // console.log(self.node)
    // console.log("weird, huh?!")
  }

  self.name_field.addEventListener("change", self.save, false);
  self.description_field.addEventListener("change", self.save, false);
  self.save_button.addEventListener("click", self.save, false);
  self.node.addEventListener("click", self.select, false);
  self.remove_button.addEventListener("click", self.destroy, false);

  $(self.node).click(function(){
    SelectPoint(self);
  })

  if(self.submitted){
    self.parentElement.appendChild(self.node);
  }
}

var debug = function(msg){
  console.log(msg);
}
