package com.idcq.appserver.common.enums;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

/**
 * 操作反馈类型枚举类
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum OptFbTypeEnum {
	
	RECEIVED("收到",1),
	CLICKED("点击",2),
	SHARED("分享",3);
	
	private String name;
	private Integer value;
	
	private OptFbTypeEnum(String name,Integer value){
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
		
		OptFbTypeEnum en = null;
		switch(value){
		case 1:
			en = RECEIVED;
			break;
		case 2:
			en = CLICKED;
			break;
		case 3:
			en = SHARED;
			break;
		default:
			throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_OPT_FB_TYPE);
			
		}
		return en.getName();
	}
	
	
	
}
