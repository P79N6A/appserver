package com.idcq.appserver.common.enums;

/**
 * 用户状态
 * 
 * @author Administrator
 * 
 */
public enum UserStatusEnum {
	WAIT_ACTIVE("待激活", 0), 
	NORMAL("正常", 1), 
	FREEZE("冻结", 2),
	LOGOUT("注销", 3);

	private String name;
	private Integer value;

	private UserStatusEnum(String name, Integer value) {
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
