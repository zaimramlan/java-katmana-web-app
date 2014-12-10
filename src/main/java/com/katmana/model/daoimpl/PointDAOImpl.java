package com.katmana.model.daoimpl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.search.exception.AssertionFailure;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.katmana.model.Point;

public class PointDAOImpl extends BaseDAOImpl<Point> implements Point.DAO{

	public PointDAOImpl(EntityManagerFactory eFactory) {
		super(eFactory);
	}

	@Override
	public List<Point> searchPoint(Map<String,String> params) {
		
		
		EntityManager em = eFactory.createEntityManager();
		FullTextEntityManager fulTextEM = Search.getFullTextEntityManager(em);
		
		em.getTransaction().begin();
		
		QueryBuilder qb = fulTextEM.getSearchFactory().buildQueryBuilder().forEntity(Point.class).get();
		
		//Basic term query must match at least one.
		BooleanJunction<BooleanJunction> bjunc = qb.bool();
		
		if(params.containsKey("search")){
			String term = params.get("search");
			bjunc = bjunc
				.should(qb.phrase().withSlop(5).onField("name").boostedTo(5).sentence(term).createQuery())
				.should(qb.phrase().withSlop(10).onField("contexts.name").boostedTo(0.8F).sentence(term).createQuery()) //Less weight with context name
				.should(qb.phrase().withSlop(10).onField("description").boostedTo(0.8F).sentence(term).createQuery()) //Less weight with description
				.should(qb.phrase().withSlop(10).onField("location_description").boostedTo(0.5F).sentence(term).createQuery()); //Even less weight with location_description
		}
		
		if(params.containsKey("context_id")){
			String context_id = params.get("context_id");
			bjunc.must(qb.keyword().onField("contexts.id").matching(context_id).createQuery());
		}

		org.apache.lucene.search.Query finalQuery;
		try{
			finalQuery = bjunc.createQuery();
		}catch(AssertionFailure e){
			finalQuery = qb.all().createQuery();
		}
		Query query = fulTextEM.createFullTextQuery(finalQuery, Point.class);
		
		List<Point> results = query.setMaxResults(1000).getResultList();
		
		em.getTransaction().commit();
		
		return results;
	}

	@Override
	public void index(Point p) {
		EntityManager em = eFactory.createEntityManager();
		FullTextEntityManager ftem = Search.getFullTextEntityManager(em);
		ftem.getTransaction().begin();
		p = ftem.merge(p);
		ftem.index(p);
		ftem.getTransaction().commit();
		em.close();
	}

}
