package com.katmana;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;

import com.github.julman99.gsonfire.GsonFireBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.katmana.model.GsonExcludeStrategy;
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
		return createGsonBuilder().create();
	}
	
	public static GsonBuilder createGsonBuilder(){
		return new GsonFireBuilder()
		.enableExposeMethodResult()
		.createGsonBuilder()
		.setExclusionStrategies(new GsonExcludeStrategy());
	}
	
	public static User getCurrentUser(HttpServletRequest request){
		return (User)request.getSession().getAttribute("user");
	}
	
	public static EntityManagerFactory getEntityManagerFactory(){
		return Persistence.createEntityManagerFactory("KatManaDB");
	}
	
	private static Validator validator;
	
	private static synchronized void createValidator(){
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	public static Validator getValidator(){
		if(validator == null){
			createValidator();
		}
		return validator;
	}

}
