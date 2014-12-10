package com.katmana.model.mockdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.katmana.model.Point;

public class PointMockDAO extends BaseMockDAO<Point> implements Point.DAO {

	public PointMockDAO() {
		/* set the data 
		 * in this case.. no data
		 */
		ArrayList<Point> data = new ArrayList<Point>();
		this.data = data;
	}

	@Override
	public List<Point> searchPoint(Map<String,String> param) {
		return null;
	}

	@Override
	public void index(Point p) {
		// TODO Auto-generated method stub
		
	}

}
