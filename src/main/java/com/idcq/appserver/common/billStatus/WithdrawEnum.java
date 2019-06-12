package com.idcq.appserver.common.billStatus;

/**
 * 用户提现账单状态
 * 
 * @author Administrator
 * 
 */
public enum WithdrawEnum {
	AUDITING("审核中", 10), 
	AUDIT_NOT_THROUGH("审核不通过", 11),
	AUDIT_PASS("审核通过，提现中",12),
	PAY_FAILURE("支付失败", 13),
	WITHDRAW_SUCCESS("已成功提现", 14);

	private String name;
	private int value;

	private WithdrawEnum(String name, int value) {
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
