$(document).on('click','#u-ico, #user-bar .icon .back-m', function() {
	$('#user-bar').toggle("slide",{direction:'right'}, 225);
	$('#u-ico').toggle("slide", {direction:'left'}, 300);
	$('#m-ico').toggle("slide", {direction:'left'}, 300);
	$('#s-ico').toggle("slide", {direction:'left'}, 300);
});

$(document).on('click','#m-ico, #menu-bar .icon .back-m', function() {
	$('#menu-bar').toggle("slide",{direction:'right'}, 225);
	$('#u-ico').toggle("slide", {direction:'left'}, 300);
	$('#m-ico').toggle("slide", {direction:'left'}, 300);
	$('#s-ico').toggle("slide", {direction:'left'}, 300);
});

$(document).on('click','#s-ico, #search-bar .icon .back-m', function() {
	$('#search-bar').toggle("slide",{direction:'right'}, 225);
	$('#u-ico').toggle("slide", {direction:'left'}, 300);
	$('#m-ico').toggle("slide", {direction:'left'}, 300);
	$('#s-ico').toggle("slide", {direction:'left'}, 300);
});

$(document).ready(function(){
	$('#menu-bar').css({display:'none'});
	$('#search-bar').css({display:'none'});
});

$(document).on('click','#u-ico-s', function() {
	$('#user-bar').css({display:'block'});
	$('#menu-bar').css({display:'none'});
	$('#search-bar').css({display:'none'});
});

$(document).on('click','#m-ico-s', function() {
	$('#user-bar').css({display:'none'});
	$('#menu-bar').css({display:'block'});
	$('#search-bar').css({display:'none'});
});

$(document).on('click','#s-ico-s', function() {
	$('#user-bar').css({display:'none'});
	$('#menu-bar').css({display:'none'});
	$('#search-bar').css({display:'block'});
});

$(window).resize(check);

function check() {
	var userNone = document.getElementById("user-bar").style.display == "none";
	var searchNone = document.getElementById("search-bar").style.display == "none";

	if (document.documentElement.clientWidth <= 614) {    
		if(document.getElementById("menu-bar") != null) {
			var menuNone = document.getElementById("menu-bar").style.display == "none";

			if(userNone && menuNone && searchNone) {
				$('#user-bar').css({display:'block'});
			}
		} else {
			if(userNone && searchNone) {
				$('#user-bar').css({display:'block'});
			}
		}
	}
}