package com.katmana.model.daoimpl;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.katmana.model.SubmitterRating;
import com.katmana.model.SubmitterRating.Summary;

public class SubmitterRatingDAOImpl extends BaseDAOImpl<SubmitterRating> implements SubmitterRating.DAO{

	public SubmitterRatingDAOImpl(EntityManagerFactory eFactory) {
		super(eFactory);
	}

	@Override
	public SubmitterRating getRating(Long rater_id, Long submitter_id) {
		Map<String,Object> query = new Hashtable<String,Object>();
		query.put("rater_id",rater_id);
		query.put("submitter_id",submitter_id);
		List<SubmitterRating> ratings = super.basicWhereQuery(query, 0, 100);
		if(ratings.size()>0){
			return ratings.get(0);
		}
		return null;
	}

	@Override
	public Summary getRatingSummary(Long submitter_id) {
		int positive = 0;
		int negative = 0;
		
		EntityManager em = eFactory.createEntityManager();
		
		List<Object[]> results = em.createQuery("SELECT count(*) as total, r.positive FROM SubmitterRating r WHERE r.submitter_id = :submitter_id GROUP BY r.positive")
				.setParameter("submitter_id", submitter_id).getResultList();
		
		for(Object[] obj:results){
			boolean is_positive = (boolean)obj[1];
			int count = (int)(long)obj[0];
			if(is_positive){
				positive = count;
			}else{
				negative = count;
			}
		}
		
		em.close();
		return new Summary(positive, negative);
	}

}
