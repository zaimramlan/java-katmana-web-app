package com.katmana;
import com.google.gson.Gson;


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

}
