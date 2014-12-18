package com.katmana.model.validation;

import javax.persistence.EntityManagerFactory;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

import com.katmana.Util;

/**
 * Copied from http://stackoverflow.com/questions/4613055/hibernate-unique-key-validation
 */
public class ConstraintValidatorFactoryImpl implements ConstraintValidatorFactory {

    private EntityManagerFactory eFactory;

    public ConstraintValidatorFactoryImpl() {
        this.eFactory = Util.getEntityManagerFactory();
    }

    @Override
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        T instance = null;

        try {
            instance = key.newInstance();
        } catch (Exception e) { 
            // could not instantiate class
            e.printStackTrace();
        }

        if(instance instanceof EntityManagerAwareValidator) {
            EntityManagerAwareValidator validator = (EntityManagerAwareValidator) instance;
            validator.setEntityManager(eFactory.createEntityManager());
        }

        return instance;
    }

	@Override
	public void releaseInstance(ConstraintValidator<?, ?> instance) {
        if(instance instanceof EntityManagerAwareValidator) {
            EntityManagerAwareValidator validator = (EntityManagerAwareValidator) instance;
            validator.closeEntityManager();
        }
	}
}