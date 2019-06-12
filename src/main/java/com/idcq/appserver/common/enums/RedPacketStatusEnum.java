package com.idcq.appserver.common.enums;
/**
 * 红包状态枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:28
 */
public enum RedPacketStatusEnum {
	
	USEABLE("可用",1),
	SHARED("已转赠",2),
	USED("已用完",3),
	EXPIRE("过期",4);
	
	private String name;
	private Integer value;
	
	private RedPacketStatusEnum(String name,Integer value){
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
