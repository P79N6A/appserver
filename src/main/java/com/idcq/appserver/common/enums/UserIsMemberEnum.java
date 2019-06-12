package com.idcq.appserver.common.enums;

/**
 * 用户状态
 * 
 * @author Administrator
 * 
 */
public enum UserIsMemberEnum {
	NOT_MEMBER("非会员", 0), 
	IS_MEMBER("是会员", 1), 
	WAIT_ACTIVE("待激活", 2);

	private String name;
	private Integer value;

	private UserIsMemberEnum(String name, Integer value) {
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
