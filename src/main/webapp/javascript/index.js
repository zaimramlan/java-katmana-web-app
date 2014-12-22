var map, isAdding = false, active_context = null, markers = new Array(), infowindow = null;
var user = null;
var current_context = null;

$.get("user/me").done(function(response){
  user = JSON.parse(response);
  populateContext({parentElement: document.getElementById("context")});
});

function get_template(name){
  return Handlebars.compile($('#'+name).html());
}

function initialize() {
  var mapOptions = {
    center: { lat: 3.25106, lng: 101.735866},
    styles: [
    {
        "featureType": "water",
        "stylers": [
            {
                "color": "#46bcec"
            },
            {
                "visibility": "on"
            }
        ]
    },
    {
        "featureType": "landscape",
        "stylers": [
            {
                "color": "#f2f2f2"
            }
        ]
    },
    {
        "featureType": "road",
        "stylers": [
            {
                "saturation": -100
            },
            {
                "lightness": 45
            }
        ]
    },
    {
        "featureType": "road.highway",
        "stylers": [
            {
                "visibility": "simplified"
            }
        ]
    },
    {
        "featureType": "road.arterial",
        "elementType": "labels.icon",
        "stylers": [
            {
                "visibility": "off"
            }
        ]
    },
    {
        "featureType": "administrative",
        "elementType": "labels.text.fill",
        "stylers": [
            {
                "color": "#444444"
            }
        ]
    },
    {
        "featureType": "transit",
        "stylers": [
            {
                "visibility": "off"
            }
        ]
    // },
    // {
    //     "featureType": "poi",
    //     "stylers": [
    //         {
    //             "visibility": "off"
    //         }
    //     ]
    }],
    zoom: 18
  };
  map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
  google.maps.event.addListener(map, 'click', function(event) {
    if(isAdding){
      $('#points .notice').remove();
      var p = new Point({
        active: false,
        deleted: false,
        submitted: false,
        name: 'New point',
        description: 'description',
        position: event.latLng,
        map: map,
        draggable: true,
        active_context: active_context,
        parentElement: document.getElementById("points"),
        infowindow: infowindow
      });
      p.save().done(function(point){
        markers.push(point);
      });
    }
    isAdding = false
    if(infowindow)
      infowindow.close();
  });

}
google.maps.event.addDomListener(window, 'load', initialize);

function clearPoints(){
  // console.log(markers)
  for(var i=0; i<markers.length; i++){
    var m = markers[i];
    if(m.deleted == false){
      m.node.parentNode.removeChild(m.node);
      m.map = null;
      if(m.marker != undefined)
        m.marker.setMap(null);
    }
    m.deleted = true;
    m = null;
  }
  markers = new Array();
}

function populateMarkers(param){
  clearPoints();

  //Clear the point list first.
  $(param.parentElement).find('.notice').remove();

  if(param.points.length != 0){
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
        draggable: param.draggable,
        name: point.name,
        description: point.description,
        active_context: active_context,
        parentElement: param.parentElement,
        point: point,
        delay: i * 100,
        infowindow: infowindow
      });
      function deferPlace(point){
        return function(){
          if(markers.indexOf(point) != -1)
            point.placeMarker();
        }
      }
      setTimeout(deferPlace(p),i*100);
      markers.push(p);
      bounds.extend(latLng);
    };
    map.fitBounds(bounds);
  }else{
    $(param.parentElement).append(get_template('no-point-notice')());
  }

  /*disable points edit if not admin*/
  if(linkContains("main.html")) setAttributesOfElements($('.points').find('input'),{"disabled":"disabled"});
}

function Context(param){
  var self = this;

  this.param = param;
  this.points = param.points;
  this.id = param.id;
  this.submitted = param.submitted;
  this.parentElement = param.parentElement;
  this.destination = param.destination;
  this.node = document.createElement("div");
  this.row_node = document.createElement("div");
  this.row_name = document.createElement("div");
  this.row_desc = document.createElement("div");
  this.row_view = document.createElement("div");
  this.container_name_description_view = document.createElement("div");
  this.container_name_description = document.createElement("div");
  this.container_name = document.createElement("div");  
  this.container_desc = document.createElement("div");  
  this.container_view = document.createElement("div");  
  this.container_menu = document.createElement("div");  
  this.name_field = document.createElement("input");
  this.description_field = document.createElement("input");
  // this.save_button = document.createElement("img"); // NOT RESOLVED
  this.back_button = document.createElement("i");
  this.display_button = document.createElement("i");
  this.remove_button = document.createElement("li");
  this.remove_button_link = document.createElement("a");
  this.menu_button = document.createElement("i");

  self.node.className = "contexts row";
  self.row_node.className = "row";
  self.row_name.className = "row";
  self.row_desc.className = "row";
  self.row_view.className = "row";
  self.container_name_description_view.className = "small-12 columns";
  self.container_name_description.className = "small-10 columns";
  self.container_name.className = "small-12 columns";
  self.container_desc.className = "small-12 columns";
  self.container_view.className = "small-2 columns";

  self.name_field.name = "name";
  self.name_field.className = "name";
  self.description_field.name = "description";
  self.description_field.className = "description";
  self.name_field.value = param.name;
  self.description_field.value = param.description;
  // self.save_button.src = "media/add.png"; // NOT RESOLVED
  self.back_button.className = "fi-arrow-left";
  self.display_button.className = "fi-arrow-right";
  self.remove_button_link.className = "delete-context-button";
  self.remove_button_link.innerHTML = "Delete";
  self.menu_button.className = "fi-braille";

  setAttributes(self.menu_button, {
    "href": "#",
    "data-options": "align:left",
    "data-dropdown": "drop"
  })

  if(strMatches(this.destination,"toContextPage")){
    self.container_name_description.className = "small-9 columns";
    self.container_view.className = "small-2 end columns";

    self.row_node.appendChild(self.container_name_description);
    self.row_node.appendChild(self.container_view);
    self.row_view.appendChild(self.display_button);

  } else if(strMatches(this.destination,"toPointsPage")) {

    self.container_name_description.className = "small-8 columns";
    self.container_menu.className = "small-2 columns";

    self.row_view.appendChild(self.back_button);
    self.row_node.appendChild(self.container_view);
    self.row_node.appendChild(self.container_name_description);
    self.row_node.appendChild(self.container_menu);
  }

  self.container_name.appendChild(self.name_field);
  self.container_desc.appendChild(self.description_field);
  self.remove_button.appendChild(self.remove_button_link);
  self.row_name.appendChild(self.container_name);
  self.row_desc.appendChild(self.container_desc);
  self.container_view.appendChild(self.row_view);
  self.container_menu.appendChild(self.menu_button);
  self.container_name_description.appendChild(self.row_name);
  self.container_name_description.appendChild(self.row_desc);
  self.container_name_description_view.appendChild(self.row_node);
  self.node.appendChild(self.container_name_description_view);

  this.display = function(param){
    this.request = $.ajax({
      type: "GET",
      url: "points",
      data: {context_id: self.id},
      dataType: "json"
    }).done(function(response){
      self.points = response;
      clearPoints();

      populateMarkers({
        draggable: true,
        points: self.points,
        parentElement: document.getElementById("points")
      });
    })

    current_context = self;
    passContext(param);
    toggleCanvasDisabler();
    togglePointsPage();
    $('.delete-context-button').remove();
    document.getElementById('drop').appendChild(self.remove_button);
    $(document).foundation();
  }

  this.save = function(){
    var description = self.description_field.value,
        name = self.name_field.value,
        method = "PUT", url = "context/"+self.id;

    if(self.submitted == false){
      method = "POST";
      url = "contexts";
    }

    $.ajax({
      type: method,
      url: url,
      data: {name: name, description: description},
      dataType: "json"
    }).done(function(response){
      self.id = response.id;
      active_context = self.id;
      self.submitted = true;
    });

    debug("context saved!");
  }

  this.destroy = function(){
    var request = $.ajax({
      type: "DELETE",
      url: "context/"+self.id,
      dataType: "json"
    });
    request.done(function(response){
      toContextPage();
    })
  }

  self.name_field.addEventListener('change', self.save, false);
  self.description_field.addEventListener('change', self.save, false);
  // self.save_button.addEventListener('click', self.save, false);
  self.display_button.addEventListener('click', self.display, false);
  self.remove_button.addEventListener('click', self.destroy, false);
  self.back_button.addEventListener('click',toContextPage, true);
  self.node.addEventListener('click',function(){
    active_context = self.id;
  },false);

  if(self.submitted == false) self.save();
  self.parentElement.appendChild(self.node);
}

function createContext(el){
  var parent = null;
  if(el != null) parent = el;
  else parent = document.getElementById("context");

  var c = new Context({
    name: 'New context',
    description: 'description',
    destination: "toPointsPage",
    submitted: false,
    parentElement: parent
  });

  clearPoints();
}

function passContext(param){
  var input = $(param.path[5]).find('input');

  var c = new Context({
    destination: "toPointsPage",
    submitted: true,
    name: input[0].value,
    description: input[1].value,
    parentElement: $('#context-header')[0].parentElement
  });
}

function populateContext(parem){
  // console.log(user.id)
  var request = $.ajax({
    type: "GET",
    url: "contexts",
    data: {submitter_id: user.id},
    dataType: "json"
  }).done(function(contexts){
    for (var i = 0; i < contexts.length; i++) {
      var context = contexts[i];
      var c = new Context({
        destination: "toContextPage",
        id: context.id,
        name: context.name,
        description: context.description,
        submitted: true,
        parentElement: parem.parentElement
      });
    };
  })
}

function togglePointsPage(){
  $('.points-page').toggleClass('move-left');
}

function setAttributesOfElements(els, attrb){
  for(var i=0; i<els.length; i++) {
    setAttributes(els[i], attrb);
  }
  debug("set finished!");
}

function setAttributes(el, attrb) {
  for(var key in attrb) {
    el.setAttribute(key, attrb[key]);
  }
}

var toContextPage = function(){
  toggleCanvasDisabler();
  togglePointsPage();
  $('.contexts').remove();
  populateContext({parentElement: document.getElementById("context")});
}

var toggleCanvasDisabler = function(param){
  if(param == "isSearching") {
    if($('.canvas-disabler').hasClass('exit-off-canvas')) {
      $('.canvas-disabler').toggleClass('exit-off-canvas');
    }
    else {
      /*do nothing*/
    }
  }
  else {
    $('.canvas-disabler').toggleClass('exit-off-canvas');
  }
}

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

var debug = function(msg){
  console.log(msg);
}