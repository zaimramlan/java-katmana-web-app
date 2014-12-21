function validate(param) {
	switch(param.name) {
		case 'name': 
			var name = param.value;
			var name_validator = document.getElementsByName('name_validator')[0];
			if(name.length<=8) $(name_validator).fadeIn(300);
			// if(name.length<=8) name_validator.style.display = "block";
			else $(name_validator).fadeOut(300);
			// else name_validator.style.display = "none";
			break;

		case 'email':
			var email = param.value;
			var email_validator = document.getElementsByName('email_validator')[0];
			if(email<=8) email_validator.style.display = "block";
			break;

		case 'password':
			var password = param.value;
			var password_validator = document.getElementsByName('password_validator')[0];
			if(password<=8) password_validator.style.display = "block";
			break;

		case 'new_password':
			var password = document.getElementsByName('password').value;
			var new_password = param.value;
			var new_password_validator = document.getElementsByName('new_password_validator')[0];
			if(new_password != password) new_password_validator.style.display = "block";
			break;		
	}
}