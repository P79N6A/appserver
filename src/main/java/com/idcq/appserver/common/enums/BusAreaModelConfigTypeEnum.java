package com.idcq.appserver.common.enums;

/**
 * 配置类型枚举类
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum BusAreaModelConfigTypeEnum {
	
	AMOUNT_PARAM("金额参数",1);
	
	private String name;
	private Integer value;
	
	private BusAreaModelConfigTypeEnum(String name,Integer value){
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
