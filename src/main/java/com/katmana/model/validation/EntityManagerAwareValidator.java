package com.katmana.model.validation;

import javax.persistence.EntityManager;

/**
 * Copied from http://stackoverflow.com/questions/4613055/hibernate-unique-key-validation
 */
public interface EntityManagerAwareValidator {
	void setEntityManager(EntityManager entityManager); 
	void closeEntityManager(); 
}
