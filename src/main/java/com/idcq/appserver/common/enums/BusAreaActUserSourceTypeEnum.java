package com.idcq.appserver.common.enums;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

/**
 * 活动会员来源类型枚举类
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum BusAreaActUserSourceTypeEnum {
	
	OTHERS("未知",0),
	SHOP_IMPORT("店铺导入",1),
	PLAT_MEMBER("平台会员",2);
	
//	OTHERS("未知",0),
//	SHOP_IMPORT("店铺导入",1),
//	SHOP_ORDER("店铺订单",2),
//	PLAT_MEMBER("平台会员",3),
//	SCAN("扫码",4),
//	WX_PUBLIC_NO("微信公众号",5);
	
	private String name;
	private Integer value;
	
	private BusAreaActUserSourceTypeEnum(String name,Integer value){
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
		
		BusAreaActUserSourceTypeEnum en = null;
		switch(value){
		case 0:
			en = OTHERS;
			break;
		case 1:
			en = SHOP_IMPORT;
			break;
		case 2:
			en = PLAT_MEMBER;
			break;
		default:
			throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_USER_SOURCE_TYPE);
			
		}
		return en.getName();
	}
//	public static String getNameByValue(Integer value){
//		if(value == null){
//			return null;
//		}
//		
//		BusAreaActUserSourceTypeEnum en = null;
//		switch(value){
//		case 0:
//			en = OTHERS;
//			break;
//		case 1:
//			en = SHOP_IMPORT;
//			break;
//		case 2:
//			en = SHOP_ORDER;
//			break;
//		case 3:
//			en = PLAT_MEMBER;
//			break;
//		case 4:
//			en = SCAN;
//		case 5:
//			en = WX_PUBLIC_NO;
//			break;
//		default:
//			throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_USER_SOURCE_TYPE);
//			
//		}
//		return en.getName();
//	}
	
	
}
