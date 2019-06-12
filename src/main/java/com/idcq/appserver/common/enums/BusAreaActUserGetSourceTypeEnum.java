package com.idcq.appserver.common.enums;

/**
 * 活动会员获取资格来源类型枚举类
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum BusAreaActUserGetSourceTypeEnum {
	
	OTHERS("未知",0),
	SHOP_IMPORT("店铺导入",1),
	ORDER("订单",2),
	REGIST("资格注册",3);
	
	private String name;
	private Integer value;
	
	private BusAreaActUserGetSourceTypeEnum(String name,Integer value){
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
