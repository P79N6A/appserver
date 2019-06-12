package com.idcq.appserver.common.billStatus;

/**
 * 用户充值账单状态
 * 
 * @author Administrator
 * 
 */
public enum RechargeEnum {
	FEEDBACK_PROCESS("待反馈进度", 30), 
	RECHARGE_SUCCESS("充值成功", 31),
	RECHARGE_FAILURE("充值失败",32);

	private String name;
	private int value;

	private RechargeEnum(String name, int value) {
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
