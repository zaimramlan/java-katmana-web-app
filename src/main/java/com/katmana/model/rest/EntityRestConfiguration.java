package com.katmana.model.rest;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.katmana.Util;
import com.katmana.model.BaseModel;
import com.katmana.model.DAOProvider;


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
			if(params.containsKey(property)){
				try {
					PropertyUtils.setProperty(record, property, params.get(property)[0]);
				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
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
				propertyList.add(prop.getName());
			}
		}
		return propertyList;
	}
	
	/**
	 * Should return a list of records that matches the request
	 * The request should contain the parameters.
	 * 
	 * @param request
	 * @return
	 */
	public abstract List<T> indexRecords(HttpServletRequest request);
	
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
}
