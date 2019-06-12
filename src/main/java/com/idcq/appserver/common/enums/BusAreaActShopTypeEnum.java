package com.idcq.appserver.common.enums;

/**
 * 活动商铺类型枚举类
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum BusAreaActShopTypeEnum {
	
	START_SHOP("发起店铺",1),
	JOIN_SHOP("参与店铺",2);
	
	private String name;
	private Integer value;
	
	private BusAreaActShopTypeEnum(String name,Integer value){
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
