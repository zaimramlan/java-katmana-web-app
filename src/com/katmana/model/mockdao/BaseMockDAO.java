package com.katmana.model.mockdao;

import java.util.ArrayList;
import java.util.Random;

import com.katmana.model.BaseModel;

/**
 * A base DAO for mocking the access for the model while the 
 * DB layer is being done.
 * 
 * @author asdacap
 */
public abstract class BaseMockDAO<T extends BaseModel> implements BaseModel.DAO<T>{

	/*
	 * Where we get the data. Subclass should populate this.
	 */
	protected ArrayList<T> data = new ArrayList<T>();

	@Override
	public boolean save(T record) {
		record.setId(new Random().nextLong()); //Lets just fill it randomly
		data.add(record);
		return true;
	}

	@Override
	public T get(Long id) {
		for(T cur:data){
			if(cur.getId().equals(id)){
				return cur;
			}
		}
		return null;
	}

	@Override
	public boolean delete(Long id) {
		for(T cur:data){
			if(cur.getId().equals(id)){
				data.remove(cur);
				return true;
			}
		}
		return false;
	}
}
