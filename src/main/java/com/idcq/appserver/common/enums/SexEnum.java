package com.idcq.appserver.common.enums;

/**
 * 性别枚举类
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum SexEnum {
	MALE("男",0),
	FEMALE("女",1),
	OTHERS("未知",2);
	
	private String name;
	private Integer value;
	
	private SexEnum(String name,Integer value){
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	
}
