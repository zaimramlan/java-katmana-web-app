package com.katmana.model.mockdao;

import java.util.ArrayList;

import com.katmana.model.PointRating;
import com.katmana.model.PointRating.Summary;

public class PointRatingMockDAO extends BaseMockDAO<PointRating> implements PointRating.DAO {

	public PointRatingMockDAO() {
		/* set the data 
		 * in this case.. no data
		 */
		ArrayList<PointRating> data = new ArrayList<PointRating>();
		this.data = data;
	}

	@Override
	public PointRating getRating(Long liker_id, Long point_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Summary getPointRatingSummary(Long point_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
