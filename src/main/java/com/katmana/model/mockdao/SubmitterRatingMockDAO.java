package com.katmana.model.mockdao;

import java.util.ArrayList;

import com.katmana.model.SubmitterRating;
import com.katmana.model.SubmitterRating.Summary;

public class SubmitterRatingMockDAO extends BaseMockDAO<SubmitterRating> implements SubmitterRating.DAO {

	public SubmitterRatingMockDAO() {
		/* set the data 
		 * in this case.. no data
		 */
		ArrayList<SubmitterRating> data = new ArrayList<SubmitterRating>();
		this.data = data;
	}

	@Override
	public SubmitterRating getRating(Long rater_id, Long point_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Summary getRatingSummary(Long point_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
