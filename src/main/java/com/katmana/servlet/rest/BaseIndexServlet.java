package com.katmana.servlet.rest;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.katmana.model.BaseModel;
import com.katmana.model.rest.EntityRestConfiguration;

/**
 * This is a base class for servlet for the '/records/' url endpoint.
 * It should implement index and create endpoint.
 * R here is the type of entity
 * T here is the EntityRestConfiguration
 * Because of the structure of servlet, to implement a rest endpoint, we need two servlet per record for two url.
 * 
 * @author asdacap
 * 
 */
public abstract class BaseIndexServlet<R extends BaseModel,T extends EntityRestConfiguration<R> > extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected Class<R> recordClass;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseIndexServlet() {
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
	 * Do index.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		T restConfiguration = getInstanceOfRestConfiguration((EntityManager)request.getAttribute("EntityManager"));
		try{
			if(!restConfiguration.allowIndex(request)){
				response.setStatus(403);
				response.getWriter().write("You do not have permission for this resource");
				return;
			}
			List<R> records = restConfiguration.indexRecords(request);
			response.setStatus(200);
			response.getWriter().write(restConfiguration.serialize(records));
		}catch(EntityRestConfiguration.RequestException e){
			response.setStatus(e.getStatusCode());
			response.getWriter().write(e.getMessage());
		}
	}

	/**
	 * Do create.
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		T restConfiguration = getInstanceOfRestConfiguration((EntityManager)request.getAttribute("EntityManager"));
		try{
			if(!restConfiguration.allowCreate(request)){
				response.setStatus(403);
				response.getWriter().write("You do not have permission for this resource");
				return;
			}
			R record;
			try {
				record = recordClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				response.sendError(500, "Record instantiation exception.");
				return;
			}

			restConfiguration.applyParams(record, request);
			//Should validate here.
			restConfiguration.doCreate(record);
			response.setStatus(201);
			response.getWriter().write(restConfiguration.serialize(record));
		}catch(EntityRestConfiguration.RequestException e){
			response.setStatus(e.getStatusCode());
			response.getWriter().write(e.getMessage());
		}
	}

}
