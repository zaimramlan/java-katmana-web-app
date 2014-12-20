package com.katmana.model.daoimpl;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.katmana.model.PointRating;
import com.katmana.model.PointRating.Summary;

public class PointRatingDAOImpl extends BaseDAOImpl<PointRating> implements PointRating.DAO{

	public PointRatingDAOImpl(EntityManager em) {
		super(em);
	}

	@Override
	public PointRating getRating(Long rater_id, Long point_id) {
		Map<String,Object> query = new Hashtable<String,Object>();
		query.put("rater_id",rater_id);
		query.put("point_id",point_id);
		List<PointRating> ratings = super.basicWhereQuery(query, 0, 100);
		if(ratings.size()>0){
			return ratings.get(0);
		}
		return null;
	}

	@Override
	public Summary getRatingSummary(Long point_id) {
		int positive = 0;
		int negative = 0;
		
		List<Object[]> results = em.createQuery("SELECT count(*) as total, r.positive FROM PointRating r WHERE r.point_id = :point_id GROUP BY r.positive")
				.setParameter("point_id", point_id).getResultList();
		
		for(Object[] obj:results){
			boolean is_positive = (boolean)obj[1];
			int count = (int)(long)obj[0];
			if(is_positive){
				positive = count;
			}else{
				negative = count;
			}
		}
		
		return new Summary(positive, negative);
	}

}
