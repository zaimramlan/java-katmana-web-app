package com.katmana.model.mockdao;

import java.util.ArrayList;

import com.katmana.model.PointContext;

public class PointContextMockDAO extends BaseMockDAO<PointContext> implements PointContext.DAO {

	public PointContextMockDAO() {
		/* set the data 
		 * in this case.. no data
		 */
		ArrayList<PointContext> data = new ArrayList<PointContext>();
		this.data = data;
	}

	@Override
	public PointContext getAssociation(Long point_id, Long context_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
