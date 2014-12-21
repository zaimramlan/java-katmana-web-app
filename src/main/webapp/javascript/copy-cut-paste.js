var selected = [];
var cut = [];
var copied = [];
var selecting = false;

var SelectPoints = function(point){
  selecting = true;
}

var CutPoints = function(){
  console.log(selected.length)
  for (var i = 0; i < selected.length; i++) {
    var new_cut = (cut.indexOf(selected[i]) == -1);
    if(new_cut){
      console.log(selected[i].id)
      cut.push(selected[i]);
      var ii = markers.indexOf(selected[i]);
      if(ii != -1){
        var m = markers[ii];
        if(m.deleted == false){
          m.node.parentNode.removeChild(m.node);
          m.map = null;
          if(m.marker != undefined)
            m.marker.setMap(null);
        }
        m.deleted = true;
        m = null;
        markers.splice(ii, 1);
      }
    }
  };
  selected = [];
  // console.log("cut")
}

var CopyPoints = function(){
  for (var i = 0; i < selected.length; i++) {
    var new_copy = (copy.indexOf(selected[i]) == -1);
    if(new_copy){
      copied.push(selected[i]);
    }
  };
  selected = [];

  // console.log("copied")
}

var PastePoints = function(){
  for (var i = 0; i < cut.length; i++) {
    var j = {id: cut[i].id, active_context: cut[i].active_context};
    (function(j){
      $.ajax({
        type: "POST",
        url: "point_contexts",
        data: {point_id: j.id, context_id: active_context}
      }).done(function(){
        $.ajax({
          type: "DELETE",
          url: "point_contexts?point_id="+j.id+"&context_id="+j.active_context
        });
      });
    })(j);
  };
  cut = [];

  for (var i = 0; i < copied.length; i++) {
    $.ajax({
      type: "POST",
      url: "point_contexts",
      data: {point_id: copied[i].id, context_id: active_context}
    }).done(function(){
      console.log("copied");
    });
  };
  current_context.display(current_context.param);

  copied = [];
  selected = [];
  selecting = false;

  console.log("pasted")
}

var SelectPoint = function(point){
  if(selecting){
    var i = selected.indexOf(point);
    $(point.node).toggleClass("selected");
    if(i != -1) {
      selected.splice(i, 1);
      // console.log("unselected")
    }
    else {
      selected.push(point);
      // console.log("selected")
    }
  }
}
