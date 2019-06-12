package com.idcq.appserver.common.enums;

/**
 * 消息通知渠道枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum MsgCenterNotifyChannelEnum {
	
	SMS("短信",1),
	JPUSH("极光推送",4),
	SHOP_MQ("店铺MQ",11),
	PUBLIC_MQ("公共MQ",12),
	CONSUMER_MQ("消费者MQ",13);
	
	private String name;
	private Integer value;
	
	private MsgCenterNotifyChannelEnum(String name,Integer value){
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
