package com.idcq.appserver.utils.jedis;

/**
 * 
 * @author Administrator
 *
 */
public class CacheData {

	private String key;
	private Object value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
}
