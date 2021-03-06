package com.katmana.model.daoimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.search.exception.AssertionFailure;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.github.julman99.gsonfire.annotations.ExposeMethodResult;
import com.katmana.model.Context;
import com.katmana.model.DAOProvider;
import com.katmana.model.Point;
import com.katmana.model.PointRating;

public class PointDAOImpl extends BaseDAOImpl<Point> implements Point.DAO{

	public PointDAOImpl(EntityManager em) {
		super(em);
	}

	@Override
	public List<Point> searchPoint(Map<String,String> params) {
		
		String countString = params.get("count");
		int count;
		if(countString == null || countString.isEmpty()){
			count = 100; // Default count
		}else{
			count = Integer.valueOf(countString);
		}
		String offsetString = params.get("offset");
		int offset;
		if(offsetString == null || offsetString.isEmpty()){
			offset = 0; // Default offset
		}else{
			offset = Integer.valueOf(offsetString);
		}
		
		FullTextEntityManager fulTextEM = Search.getFullTextEntityManager(em);
		
		QueryBuilder qb = fulTextEM.getSearchFactory().buildQueryBuilder().forEntity(Point.class).get();
		
		//Basic term query must match at least one.
		BooleanJunction<BooleanJunction> bjunc = qb.bool();
		
		if(params.containsKey("search") && !params.get("search").isEmpty()){
			String term = params.get("search");
			bjunc = bjunc
				.should(qb.phrase().withSlop(5).onField("name").boostedTo(5).sentence(term).createQuery())
				.should(qb.phrase().withSlop(10).onField("contexts.description").boostedTo(0.3F).sentence(term).createQuery()) // Less weight with context description
				.should(qb.phrase().withSlop(10).onField("contexts.name").boostedTo(0.8F).sentence(term).createQuery()) //Less weight with context name
				.should(qb.phrase().withSlop(10).onField("description").boostedTo(0.8F).sentence(term).createQuery()) //Less weight with description
				.should(qb.keyword().fuzzy().onField("name").andField("description").boostedTo(0.3F).matching(term).createQuery()) //If query on multiple field
				.should(qb.keyword().onField("name").andField("description").boostedTo(0.5F).matching(term).createQuery()) //If query on multiple field
				.should(qb.keyword().onField("name").andField("description").andField("contexts.name").andField("contexts.description").boostedTo(0.5F).matching(term).createQuery()) //If query on multiple field
				.should(qb.phrase().withSlop(10).onField("location_description").boostedTo(0.5F).sentence(term).createQuery()); //Even less weight with location_description
		}
		
		if(params.containsKey("context_id")){
			String context_id = params.get("context_id");
			bjunc.must(qb.keyword().onField("contexts.id").matching(context_id).createQuery());
		}
		
		//Queriables/Filter
		String[] queriables = {"submitter_id","name","description","location_description"};
		for(String param:queriables){
			if(params.containsKey(param)){
				bjunc.must(qb.keyword().onField(param).matching(params.get(param)).createQuery());
			}
		}

		org.apache.lucene.search.Query finalQuery;
		try{
			finalQuery = bjunc.createQuery();
		}catch(AssertionFailure e){
			finalQuery = qb.all().createQuery();
		}
		Query query = fulTextEM.createFullTextQuery(finalQuery, Point.class);
		
		List<Point> results = query.setMaxResults(count).setFirstResult(offset).getResultList();
		
		
		return results;
	}

	@Override
	public void index(Point p) {
		FullTextEntityManager ftem = Search.getFullTextEntityManager(em);
		p = ftem.merge(p);
		ftem.index(p);
	}
	
}
