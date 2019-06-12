package com.idcq.appserver.common.enums;

/**
 * 活动操作类型
 * @author Administrator
 *
 */
public enum BusAreaOperateTypeEnum {
	JOIN_ACTIVITY(1),
	CANCEL_BY_JOINER(2),
	CANCEL_BY_ORIGINATE(3);
	
	private Integer index;
	
	public Integer getIndex() {
		return index;
	}

	private BusAreaOperateTypeEnum(Integer index) {
		this.index = index;
	}
	
}
