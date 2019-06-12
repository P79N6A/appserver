package com.idcq.appserver.common.enums;

/**
 * 消息状态枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum MsgCenterMsgStatusEnum {
	
	SENDING("发送中",0),
	SEND_OK("已发送",1),
	NOT_SEND("未发送",2);
	
	private String name;
	private Integer value;
	
	private MsgCenterMsgStatusEnum(String name,Integer value){
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
