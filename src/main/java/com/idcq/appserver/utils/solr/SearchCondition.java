package com.idcq.appserver.utils.solr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 查询条件类型
 * @author Administrator
 *
 */
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface SearchCondition {
	
	/**
	 * 查询类型
	 * @return
	 */
	SearchType seachType();
	
	/**
	 * in方式查询关联的字段
	 * @return
	 */
	String searchInRelateField() default "";
	
	/**
	 * 排序字段
	 * @return
	 */
	String orderByField() default "";
	
	boolean store() default true;
	
}
