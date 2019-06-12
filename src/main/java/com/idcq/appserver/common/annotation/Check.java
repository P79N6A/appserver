package com.idcq.appserver.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface Check {

	boolean required() default true;
	boolean recurse() default false;
	boolean sensitive() default false;
	String pattern() default "";
}
