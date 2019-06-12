package com.idcq.appserver.common.enums;

/**
 * 活动商铺状态枚举类
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum BusAreaActShopStatusEnum {
	
	INVITE_NOTIFY("邀约通知",1),
	READ("阅读",2),
	JOIN("加入",3),
	EXIT("退出",4);
	
	private String name;
	private Integer value;
	
	private BusAreaActShopStatusEnum(String name,Integer value){
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
