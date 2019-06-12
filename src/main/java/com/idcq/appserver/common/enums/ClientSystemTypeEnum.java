package com.idcq.appserver.common.enums;

import org.apache.commons.lang3.StringUtils;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

/**
 * 终端类型枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum ClientSystemTypeEnum {
	CASHIER("收银机",1),
	YD_MGR("一点管家",2),
	CONSUMER_APP("消费者APP",3),
	WX_MALL("微信商城",4),
	PUBLIC_NO("公众号",5),
	SHOP_BACKEND("商铺后台",6),
	CASHIER_PAD("收银机PAD",7),
	ROUTER("路由器",8);
	
	private String name;
	private Integer value;
	
	private ClientSystemTypeEnum(String name,Integer value){
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
	
	public static Integer getValueByName(String name){
		if(StringUtils.isBlank(name)){
			return null;
		}
		
		ClientSystemTypeEnum en = null;
		switch(name){
			case "收银机":
				en = CASHIER;
				break;
			case "收银机PAD":
				en = CASHIER_PAD;
				break;
			case "消费者APP":
				en = CONSUMER_APP;
				break;
			case "一点管家":
				en = YD_MGR;
				break;
			case "路由器":
				en = ROUTER;
				break;
			case "微信商城":
				en = WX_MALL;
				break;
			case "公众号":
				en = PUBLIC_NO;
				break;
			case "商铺后台":
				en = SHOP_BACKEND;
				break;
			default:
				throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_CLIENT_TYPE);
					
		}
		return en.getValue();
	}
	
	public static String getNameByValue(Integer value){
		if(value == null){
			return null;
		}
		
		ClientSystemTypeEnum en = null;
		switch(value){
		case 1:
			en = CASHIER;
			break;
		case 2:
			en = YD_MGR;
			break;
		case 3:
			en = CONSUMER_APP;
			break;
		case 4:
			en = WX_MALL;
			break;
		case 5:
			en = PUBLIC_NO;
			break;
		case 6:
			en = SHOP_BACKEND;
			break;
		case 7:
			en = CASHIER_PAD;
			break;
		case 8:
			en = ROUTER;
			break;
		default:
			throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_CLIENT_TYPE);
			
		}
		return en.getName();
	}
	
	
	
}
