package com.katmana.model.mockdao;

import java.util.ArrayList;

import com.katmana.model.SubmitterRating;

public class SubmitterRatingMockDAO extends BaseMockDAO<SubmitterRating> implements SubmitterRating.DAO {

	public SubmitterRatingMockDAO() {
		/* set the data 
		 * in this case.. no data
		 */
		ArrayList<SubmitterRating> data = new ArrayList<SubmitterRating>();
		this.data = data;
	}

}
