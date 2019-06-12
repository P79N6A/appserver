package com.idcq.appserver.common.enums;

/**
 * 消息通知方式枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum MsgCenterNotifyModelEnum {
	
	END2END("点对点",1),
	NOTICE("公告",2);
	
	private String name;
	private Integer value;
	
	private MsgCenterNotifyModelEnum(String name,Integer value){
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
