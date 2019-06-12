package com.idcq.appserver.common.billStatus;

/**
 * 用户奖励账单状态
 * 
 * @author Administrator
 * 
 */
public enum RewardsEnum {
	HAVE_NOT_SETTLEMENT("未结算", 40), 
	HAVE_SETTLEMENT("已结算",41);

	private String name;
	private int value;

	private RewardsEnum(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Integer getValue() {
		return value;
	}
	
}
