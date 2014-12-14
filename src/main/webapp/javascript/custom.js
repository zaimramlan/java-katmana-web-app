// $(document).ready(dot);
$(document).on('click','.trig,.trigs',dot);

function dot() {
	// $('.trig').click(function(){
	// 	// $('#map-canvas').toggle("drop",{direction:'up'});
	// 	$('#right-col').toggle("drop",{direction:right});
	// });
	$('#right-col').toggle("drop",{direction:'right'});
}