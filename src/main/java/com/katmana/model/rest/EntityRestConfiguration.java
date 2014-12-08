package com.katmana.model.rest;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.google.common.base.CaseFormat;
import com.katmana.Util;
import com.katmana.model.BaseModel;
import com.katmana.model.DAOProvider;
import com.katmana.model.User;


/**
 * The purpose of this class is to encapsulate common REST operation for a record
 * This class will be used by the base index servlets
 * 
 * @author asdacap
 *
 */
public abstract class EntityRestConfiguration<T extends BaseModel> {
	
	protected BaseModel.DAO<T> dao;
	protected Class<T> entityClass;
	
	/**
	 * A default constructor is required by BaseIndexServlet
	 */
	public EntityRestConfiguration(){
		this.entityClass = ((Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0]);
		dao = (BaseModel.DAO<T>)DAOProvider.getInstance().getDaoByType(this.entityClass);
	}
	
	/**
	 * These two function are util function that can be override
	 * by subclass to provide customizations needed.
	 * This one accept a list and should convert it to json.
	 * 
	 * @param list
	 * @return
	 */
	public String serialize(List<T> list){
		return Util.createGson().toJson(list);
	}
	
	/**
	 * This one accept a record and should convert it to json.
	 * @param record
	 * @return
	 */
	public String serialize(T record){
		return Util.createGson().toJson(record);
	}
	
	/**
	 * Apply the parameter from request to record.
	 * Used by default create and update implementation in servlet.
	 * By default will put the property if available.
	 * Property list obtained by getRecordProperties().
	 * 
	 * @param record
	 * @param request
	 */
	public void applyParams(T record, HttpServletRequest request){
		Map<String,String[] > params = request.getParameterMap();
		for(String property:getWritableRecordProperties()){
			//To set it in beans, we need the camelCase property
			String propName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, property);
			if(params.containsKey(property)){
				try {
					BeanUtils.setProperty(record, propName, params.get(property)[0]);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * This function should return T's bean property that is writable.
	 * It need to be writable because this is used by applyParams
	 * @return
	 */
	public List<String> getWritableRecordProperties(){
		PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(entityClass);
		ArrayList<String> propertyList = new ArrayList<String>();
		for(PropertyDescriptor prop:properties){
			if(prop.getWriteMethod() != null){
				//Because prob.getName is in camelCase, but the property (request and json) is using snake_case
				String propName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, prop.getName());
				propertyList.add(propName);
			}
		}
		return propertyList;
	}
    
    /**
     * Get the id from the extra path.
     * @return
     */
    public long getId(HttpServletRequest request){
    	String extra = request.getPathInfo();
    	extra = extra.substring(1); // Remote the initial '/'
    	return Long.valueOf(extra);
    }
	
	/**
	 * Should return a list of records that matches the request
	 * The request should contain the parameters.
	 * 
	 * @param request
	 * @return
	 */
	public List<T> indexRecords(HttpServletRequest request){
		String countString = request.getParameter("count");
		int count;
		if(countString == null || countString.isEmpty()){
			count = 100; // Default count
		}else{
			count = Integer.valueOf(countString);
		}
		String offsetString = request.getParameter("offset");
		int offset;
		if(offsetString == null || offsetString.isEmpty()){
			offset = 0; // Default offset
		}else{
			offset = Integer.valueOf(offsetString);
		}
		
		return dao.listAll(offset, count);
	}
	
	/**
	 * Override this to define if the index endpoint is allowed for this resource.
	 * Default is to delegate to User variant.
	 * @param request
	 * @return
	 */
	public boolean allowIndex(HttpServletRequest request){
		return allowIndex(Util.getCurrentUser(request));
	}

	/**
	 * Override this to define if the index endpoint is allowed for this resource
	 * for this currentUser
	 * Default is to delegate to allowResource
	 * @param request
	 * @return
	 */
	public boolean allowIndex(User currentUser){
		return allowResource(currentUser);
	}
	
	/**
	 * Called by servlet on create operation.
	 * This function should save the record and return true
	 * if successfully created.
	 * Default implementation is to delegate it to DAO
	 * 
	 * @param request
	 * @return
	 */
	public boolean doCreate(T record){
		return dao.save(record);
	}
	
	/**
	 * Override this to define if the create endpoint is allowed for this resource.
	 * Default is to delegate to User variant.
	 * @param request
	 * @return
	 */
	public boolean allowCreate(HttpServletRequest request){
		return allowCreate(Util.getCurrentUser(request));
	}

	/**
	 * Override this to define if the create endpoint is allowed for this resource
	 * for this currentUser
	 * Default is to delegate to allowResource
	 * @param request
	 * @return
	 */
	public boolean allowCreate(User currentUser){
		return allowResource(currentUser);
	}
	
	/**
	 * Called by various method. Should return a record with the specific id
	 * if exist. null otherwhise. 
	 * Default implementation is to delegate it to DAO
	 * 
	 * @param request
	 * @return
	 */
	public T getRecord(Long id){
		return dao.get(id);
	}
	
	/**
	 * Override this to define if the show endpoint is allowed for this resource.
	 * Default is to delegate to User variant.
	 * @param request
	 * @return
	 */
	public boolean allowShow(HttpServletRequest request){
		return allowShow(getRecord(getId(request)),Util.getCurrentUser(request));
	}

	/**
	 * Override this to define if the show endpoint is allowed for this resource
	 * for this currentUser
	 * Default is to delegate to allowResource
	 * @param request
	 * @return
	 */
	public boolean allowShow(T record,User currentUser){
		return allowResource(currentUser);
	}

	/**
	 * Called by servlet on update operation.
	 * Should return a boolean indicating the success of update.
	 * Default implementation is to delegate it to DAO
	 * 
	 * @param request
	 * @return
	 */
	public boolean doUpdate(T record){
		return dao.update(record);
	}
	
	/**
	 * Override this to define if the update endpoint is allowed for this resource.
	 * Default is to delegate to User variant.
	 * @param request
	 * @return
	 */
	public boolean allowUpdate(HttpServletRequest request){
		return allowUpdate(getRecord(getId(request)),Util.getCurrentUser(request));
	}

	/**
	 * Override this to define if the update endpoint is allowed for this resource
	 * for this currentUser
	 * Default is to delegate to allowModify
	 * @param request
	 * @return
	 */
	public boolean allowUpdate(T record,User currentUser){
		return allowModify(record,currentUser);
	}

	/**
	 * Called by servlet on destroy uperation.
	 * Should return a boolean indicating the success of deletion.
	 * Default implementation is to delegate it to DAO
	 * 
	 * @param request
	 * @return
	 */
	public boolean doDestroy(T record){
		return dao.delete(record);
	}
	
	/**
	 * Override this to define if the destroy endpoint is allowed for this resource.
	 * Default is to delegate to User variant.
	 * @param request
	 * @return
	 */
	public boolean allowDestroy(HttpServletRequest request){
		return allowDestroy(getRecord(getId(request)),Util.getCurrentUser(request));
	}

	/**
	 * Override this to define if the destroy endpoint is allowed for this resource
	 * for this currentUser
	 * Default is to delegate to allowModify
	 * @param request
	 * @return
	 */
	public boolean allowDestroy(T record,User currentUser){
		return allowModify(record,currentUser);
	}
	

	/**
	 * Override this to define if the currentUser is allowed to modify the record.
	 * This is default implementation for allowUpdate and allowDestroy
	 * default is to delegate to allowResource
	 * @param record
	 * @param currentUser
	 * @return
	 */
	public boolean allowModify(T record,User currentUser){
		return allowResource(currentUser);
	}
	
	
	/**
	 * A catch all on the default implementation. Subclass may override this method do
	 * forbid access by default.
	 * @param currentUser
	 * @return
	 */
	public boolean allowResource(User currentUser){
		return true;
	}
}
