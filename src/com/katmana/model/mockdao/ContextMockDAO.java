package com.katmana.model.mockdao;

import java.util.ArrayList;

import com.katmana.model.Context;

public class ContextMockDAO extends BaseMockDAO<Context> implements Context.DAO {

	public ContextMockDAO() {
		/* set the data 
		 * in this case.. no data
		 */
		ArrayList<Context> data = new ArrayList<Context>();
		this.data = data;
	}

	@Override
	public Context getByName(String name) {
		for(Context cur:data){
			if(cur.getName().equals(name)){
				return cur;
			}
		}
		return null;
	}

}
