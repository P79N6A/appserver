package com.idcq.appserver.common.enums;

/**
 * 活动商铺加入方式枚举类
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum BusAreaActShopJoinTypeEnum {
	
	MGR_PUSH("管家推送加入",0),
	WX_SHARED("微信分享加入",1),
	CASHIER_PUSH("收银机推送加入",2);
	
	private String name;
	private Integer value;
	
	private BusAreaActShopJoinTypeEnum(String name,Integer value){
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
