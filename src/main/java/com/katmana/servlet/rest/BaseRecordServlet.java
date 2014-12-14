package com.katmana.servlet.rest;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public abstract class BaseRecordServlet<R extends BaseModel,T extends EntityRestConfiguration<R> > extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected T restConfiguration;
	protected Class<R> recordClass;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseRecordServlet() {
        super();
        restConfiguration = getInstanceOfT();
        
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
    protected T getInstanceOfT()
    {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[1]; //1 as in the second parameter
        try
        {
            return type.newInstance();
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
		try{
			R record = restConfiguration.getRecord(request);
			if(record == null){
				response.setStatus(404);
				response.getWriter().write("Nothing to see here.");
				return;
			}
			if(!restConfiguration.allowShow(request)){
				response.setStatus(403);
				response.getWriter().write("You do not have permission for this resource");
				return;
			}
			response.setStatus(200);
			response.getWriter().write(restConfiguration.serialize(record));
		}catch(EntityRestConfiguration.RequestException e){
			response.setStatus(e.getStatusCode());
			response.getWriter().write(e.getMessage());
		}
	}

	/**
	 * Do update.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			R record = restConfiguration.getRecord(request);
			if(record == null){
				response.setStatus(404);
				response.getWriter().write("Nothing to see here.");
				return;
			}
			if(!restConfiguration.allowUpdate(request)){
				response.setStatus(403);
				response.getWriter().write("You do not have permission for this resource");
				return;
			}
			restConfiguration.applyParams(record, request);
			restConfiguration.doUpdate(record);
			response.setStatus(202);
			response.getWriter().write(restConfiguration.serialize(record));
		}catch(EntityRestConfiguration.RequestException e){
			response.setStatus(e.getStatusCode());
			response.getWriter().write(e.getMessage());
		}
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
		try{
			R record = restConfiguration.getRecord(request);
			if(record == null){
				response.setStatus(404);
				response.getWriter().write("Nothing to see here.");
				return;
			}
			if(!restConfiguration.allowDestroy(request)){
				response.setStatus(403);
				response.getWriter().write("You do not have permission for this resource");
				return;
			}
			restConfiguration.doDestroy(record);
			response.setStatus(204);
			response.getWriter().write(restConfiguration.serialize(record));
		}catch(EntityRestConfiguration.RequestException e){
			response.setStatus(e.getStatusCode());
			response.getWriter().write(e.getMessage());
		}
	}
	
}
