package com.katmana.model.validation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.katmana.model.User;

/**
 * Copied and modified from http://stackoverflow.com/questions/4613055/hibernate-unique-key-validation
 */
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, User>, EntityManagerAwareValidator {

    private EntityManager entityManager;

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(User target, ConstraintValidatorContext context) {
    	
    	//If it is loaded by JPA, this won't work.
    	if(entityManager == null){
    		return true;
    	}
    	
    	Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.id != :id");
    	query.setParameter("email", target.getEmail());
    	query.setParameter("id", target.getId());
    	
        List<?> resultSet = query.getResultList();

        return resultSet.size() == 0;
    }

	@Override
	public void closeEntityManager() {
		entityManager.close();
	}

}
