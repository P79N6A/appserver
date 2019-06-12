package com.idcq.appserver.common.enums;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

/**
 * 活动状态枚举类
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum BusAreaActStatusEnum {
	
	NOT_START("未开始",0),
	APPLYING("报名中",1),
	RUNNING("进行中",2),
	STOP("结束",3),
	APPLY_END("报名截止",4);
	private String name;
	private Integer value;
	
	private BusAreaActStatusEnum(String name,Integer value){
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
	
	public static String getNameByValue(Integer value){
		if(value == null){
			return null;
		}
		
		BusAreaActStatusEnum en = null;
		switch(value){
		case 0:
			en = NOT_START;
			break;
		case 1:
			en = APPLYING;
			break;
		case 2:
			en = RUNNING;
			break;
		case 3:
			en = STOP;
			break;
		case 4:
			en = APPLY_END;
			break;
		default:
			throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_CLIENT_TYPE);
		}
		return en.getName();
	}
	
}
