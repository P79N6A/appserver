package com.idcq.appserver.common.enums;

/**
 * 消息发送结果枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum MsgCenterSendResultEnum {
	
	
	
	FAIL("失败",0),
	SUCCESS("成功",1);
	
	private String name;
	private Integer value;
	
	private MsgCenterSendResultEnum(String name,Integer value){
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
