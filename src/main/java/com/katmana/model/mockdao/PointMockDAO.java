package com.katmana.model.mockdao;

import java.util.ArrayList;
import java.util.List;

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
	public List<Point> searchPoint(String term) {
		return null;
	}

}
