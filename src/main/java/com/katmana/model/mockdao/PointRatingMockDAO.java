package com.katmana.model.mockdao;

import java.util.ArrayList;

import com.katmana.model.PointRating;

public class PointRatingMockDAO extends BaseMockDAO<PointRating> implements PointRating.DAO {

	public PointRatingMockDAO() {
		/* set the data 
		 * in this case.. no data
		 */
		ArrayList<PointRating> data = new ArrayList<PointRating>();
		this.data = data;
	}

}
