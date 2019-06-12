package com.idcq.appserver.common.enums;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

/**
 * 消息回馈结果类型枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum MsgCenterFBTypeEnum {
	
	NOT_FB("无回馈或无需回馈",0),
	READ("已读",1),
	CLICKED("已点击",2),
	SHARED("分享",3);
	
	private String name;
	private Integer value;
	
	private MsgCenterFBTypeEnum(String name,Integer value){
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
		
		MsgCenterFBTypeEnum en = null;
		switch(value){
		case 0:
			en = NOT_FB;
			break;
		case 1:
			en = READ;
			break;
		case 2:
			en = CLICKED;
			break;
		case 3:
			en = SHARED;
			break;
		default:
			throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_CLIENT_TYPE);
			
		}
		return en.getName();
	}
	
	
}
