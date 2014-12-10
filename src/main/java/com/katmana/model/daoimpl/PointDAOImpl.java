package com.katmana.model.daoimpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.katmana.model.Point;

public class PointDAOImpl extends BaseDAOImpl<Point> implements Point.DAO{

	public PointDAOImpl(EntityManagerFactory eFactory) {
		super(eFactory);
	}

	@Override
	public List<Point> searchPoint(String term) {
		
		EntityManager em = eFactory.createEntityManager();
		FullTextEntityManager fulTextEM = Search.getFullTextEntityManager(em);
		
		em.getTransaction().begin();
		
		QueryBuilder qb = fulTextEM.getSearchFactory().buildQueryBuilder().forEntity(Point.class).get();
		
		//Basic term query must match at least one.
		org.apache.lucene.search.Query phraseQuery = qb.bool()
				.should(qb.phrase().withSlop(5).onField("name").boostedTo(5).sentence(term).createQuery())
				.should(qb.phrase().withSlop(10).onField("description").boostedTo(0.8F).sentence(term).createQuery()) //Less weight with description
				.should(qb.phrase().withSlop(10).onField("location_description").boostedTo(0.5F).sentence(term).createQuery()) //Even less weight with location_description
				.createQuery();
		
		Query query = fulTextEM.createFullTextQuery(phraseQuery, Point.class);
		
		List<Point> results = query.setMaxResults(1000).getResultList();
		
		em.getTransaction().commit();
		
		return results;
	}

}
