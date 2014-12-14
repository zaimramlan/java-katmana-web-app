package com.katmana.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field marked by this will not be serialized by GSON.
 * We did not use the transient keywords as that will also result in
 * Hibernate not saving the field.
 * @author asdacap
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcludeJson {

}
