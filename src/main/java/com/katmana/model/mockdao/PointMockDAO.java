package com.katmana.model.mockdao;

import java.util.ArrayList;

import com.katmana.model.Point;

public class PointMockDAO extends BaseMockDAO<Point> implements Point.DAO {

	public PointMockDAO() {
		/* set the data 
		 * in this case.. no data
		 */
		ArrayList<Point> data = new ArrayList<Point>();
		this.data = data;
	}

}
