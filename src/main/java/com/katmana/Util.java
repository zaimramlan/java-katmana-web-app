package com.katmana;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;

import com.github.julman99.gsonfire.GsonFireBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.katmana.model.GsonExcludeStrategy;
import com.katmana.model.User;
import com.katmana.model.validation.ConstraintValidatorFactoryImpl;


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
	
	private static EntityManagerFactory eFactory;
	
	public static synchronized void createEntityManagerFactory(){
		eFactory = Persistence.createEntityManagerFactory("KatManaDB");
	}
	
	public static EntityManagerFactory getEntityManagerFactory(){
		if(eFactory == null){
			createEntityManagerFactory();
		}
		return eFactory;
	}
	
	private static Validator validator;
	
	private static synchronized void createValidator(){
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		ValidatorContext validatorContext = validatorFactory.usingContext();
		validatorContext.constraintValidatorFactory(new ConstraintValidatorFactoryImpl());
		validator = validatorContext.getValidator();
		//validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	public static Validator getValidator(){
		if(validator == null){
			createValidator();
		}
		return validator;
	}

}
