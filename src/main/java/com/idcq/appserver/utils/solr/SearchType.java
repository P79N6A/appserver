package com.idcq.appserver.utils.solr;
/**
 * 查询类型
 * @author pchzhang
 *
 */
public enum SearchType {
	/**
	 * IN 方式
	 */
	IN(),
	
	/**
	 * 等于方式
	 */
	EQUALS(),
	
	/**
	 * 分词方式查询
	 */
	KEYWORD(),
	
	/**
	 * 地理位置搜索
	 */
	LOCATIONSEARCH(),
	
	/**
	 * 排序
	 */
	ORDERBY(),
	
	/**
	 * 地理位置排序
	 */
	LOCATIONORDERBY(),
	
	/**
	 * 分组查询
	 */
	GROUPSEARCH(),
	
	/**
	 * 大于等于
	 */
	GT();
}
