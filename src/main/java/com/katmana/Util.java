package com.katmana;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.katmana.model.User;


/**
 * A class that holds utility function.
 * It should only have static function.
 * @author asdacap
 *
 */

public class Util {
	private Util(){} //What instantiation? No need la.
	
	/**
	 * Because a Gson can have configurations, it better if we create 
	 * a function that return a Gson with common configuration.
	 * 
	 * @return
	 */
	public static Gson createGson(){
		return new Gson();
	}
	
	public static User getCurrentUser(HttpServletRequest request){
		return (User)request.getSession().getAttribute("user");
	}

}
