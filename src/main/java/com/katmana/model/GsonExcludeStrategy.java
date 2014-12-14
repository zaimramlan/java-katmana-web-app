package com.katmana.model;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.katmana.model.annotation.ExcludeJson;

public class GsonExcludeStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes arg0) {
		return arg0.getAnnotation(ExcludeJson.class) != null;
	}

}
