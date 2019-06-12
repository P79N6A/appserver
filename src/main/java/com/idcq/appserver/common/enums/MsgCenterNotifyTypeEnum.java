package com.idcq.appserver.common.enums;

/**
 * 消息通知类型枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum MsgCenterNotifyTypeEnum {
	
	NOTITY_USER("通知商铺会员",1),
	NOTITY_SHOP("通知商铺",2),
	NOTITY_PLATFORM_USER("通知平台会员",3);
	
	private String name;
	private Integer value;
	
	private MsgCenterNotifyTypeEnum(String name,Integer value){
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
