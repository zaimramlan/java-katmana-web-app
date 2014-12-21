package com.katmana.servlet.rest;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.katmana.Util;
import com.katmana.model.BaseModel;
import com.katmana.model.rest.EntityRestConfiguration;

/**
 * This is a base class for servlet for the '/records/id' url endpoint.
 * It should implement show,update and delete
 * R here is the type of entity
 * T here is the EntityRestConfiguration
 * Because of the structure of servlet, to implement a rest endpoint, we need two servlet per record for two url.
 * 
 * @author asdacap
 * 
 */
public abstract class BaseRecordServlet<R extends BaseModel,T extends EntityRestConfiguration<R> > extends BaseRestServlet {
	private static final long serialVersionUID = 1L;
       
	protected Class<R> recordClass;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseRecordServlet() {
        super();
        
        /*
         * These two create a recordClass so that we can instantiate it.
         */
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        recordClass = (Class<R>) superClass.getActualTypeArguments()[0]; //0 as in the first parameter
    }
    
    /**
     * Copied from stackoverflow, this create a new instance of T
     * @return
     */
    protected T getInstanceOfRestConfiguration(EntityManager em)
    {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[1]; //1 as in the second parameter
        try
        {
            return type.getDeclaredConstructor(EntityManager.class).newInstance(em);
        }
        catch (Exception e)
        {
            // Oops, no default constructor
            throw new RuntimeException(e);
        }
    }
    
	/**
	 * Do show.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		T restConfiguration = getInstanceOfRestConfiguration((EntityManager)request.getAttribute("EntityManager"));
		R record = restConfiguration.getRecord(request);
		if(record == null){
			throw new EntityRestConfiguration.RequestException("Nothing to see here.",404);
		}
		if(!restConfiguration.allowShow(request)){
			throw new EntityRestConfiguration.RequestException("You do not have permission for this resource",401);
		}
		response.setStatus(200);
		response.getWriter().write(restConfiguration.serialize(record,request));
	}

	/**
	 * Do update.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		T restConfiguration = getInstanceOfRestConfiguration((EntityManager)request.getAttribute("EntityManager"));
		R record = restConfiguration.getRecord(request);
		if(record == null){
			throw new EntityRestConfiguration.RequestException("Nothing to see here.",404);
		}
		if(!restConfiguration.allowUpdate(request)){
			throw new EntityRestConfiguration.RequestException("You do not have permission for this resource",401);
		}
		restConfiguration.applyParams(record, request);

		//Validate it
		Set<ConstraintViolation<R> > violations = Util.getValidator().validate(record);
		if(violations.size() > 0){
			throw new ConstraintViolationException(violations);
		}

		restConfiguration.doUpdate(record);
		response.setStatus(202);
		response.getWriter().write(restConfiguration.serialize(record,request));

	}
	
	/**
	 * Do update
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * Do delete.
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		T restConfiguration = getInstanceOfRestConfiguration((EntityManager)request.getAttribute("EntityManager"));
		R record = restConfiguration.getRecord(request);
		if(record == null){
			throw new EntityRestConfiguration.RequestException("Nothing to see here.",404);
		}
		if(!restConfiguration.allowDestroy(request)){
			throw new EntityRestConfiguration.RequestException("You do not have permission for this resource",401);
		}
		restConfiguration.doDestroy(record);
		response.setStatus(204);
		response.getWriter().write(restConfiguration.serialize(record,request));
	}
	
}
