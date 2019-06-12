package com.idcq.appserver.common.billStatus;

/**
 * 用户消费账单状态
 * @author Administrator
 *
 */
public enum ConsumeEnum {
	ORDERED("已预订", 20), 
	HAVE_ORDER("已开单", 21),
	DISPATCH("派送中",22),
	CLOSED_ACCOUNT("已完成", 23),
	CHARGEBACKING("退单中", 24),
	HAVE_CHARGEBACK("已退单", 25),
	NO_SETTLE("未结算", 40);
	

	private String name;
	private int value;

	private ConsumeEnum(String name, int value) {
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
