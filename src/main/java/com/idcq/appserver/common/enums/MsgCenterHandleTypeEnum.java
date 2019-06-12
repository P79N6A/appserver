package com.idcq.appserver.common.enums;

/**
 * 消息操作类型枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum MsgCenterHandleTypeEnum {
	
	DISPLAY("显示",1),
	DEL_AFTER_READ("阅后即焚",2),
	NOT_DELAY("过时不候",4),
	DEL_OLD("删除之前的消息",8),
	NEED_FEEDBACK("需要回馈",16),
	AUTO_EXEC_LOCAL("自动执行本地操作",32),
	CLICK_EXEC_LOCAL("点击执行本地操作",64);
	
	private String name;
	private Integer value;
	
	private MsgCenterHandleTypeEnum(String name,Integer value){
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
