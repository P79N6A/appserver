package com.idcq.appserver.common.enums;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

/**
 * 主键类型枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum BizTypeEnum {
	
	SHOP("商铺",1),
	USER("用户",2),
	PATTERN("模板",3),
	USER_SERVICE_PROTOCOL("用户服务协议",4),
	SHOP_SERVICE_PROTOCOL("商家服务协议",5),
	COUPON("代金券",6),
	BANK("银行",7),
	GOODS("商品",8),
	GOODS_GROUP("商品族",9),
	TECHNICIAN("技师",10),
	GOODS_CATEGORY("商品分类",11),
	LAUNCHER_ICON("launcher主页图标",12),
	BUSAREA_ACTIVITY("商圈活动",13),
	CASHIER_LOG("收银机日志",14),
	BUSAREA_ACTIVITY_TYPE("商圈活动类型",15),
	MSG_CENTER_MSG("消息中心消息",16),
	SHOP_FEEDBACK("商铺反馈",20);
	
	private String name;
	private Integer value;
	
	public static String getNameByValue(Integer value){
		if(value == null){
			return null;
		}
		
		BizTypeEnum en = null;
		switch(value){
		case 1:
			en = SHOP;
			break;
		case 2:
			en = USER;
			break;
		case 3:
			en = PATTERN;
			break;
		case 4:
			en = USER_SERVICE_PROTOCOL;
			break;
		case 5:
			en = SHOP_SERVICE_PROTOCOL;
			break;
		case 6:
			en = COUPON;
			break;
		case 7:
			en = BANK;
			break;
		case 8:
			en = GOODS;
			break;
		case 9:
			en = GOODS_GROUP;
			break;
		case 10:
			en = TECHNICIAN;
			break;
		case 11:
			en = GOODS_CATEGORY;
			break;
		case 12:
			en = LAUNCHER_ICON;
			break;
		case 13:
			en = BUSAREA_ACTIVITY;
			break;
		case 14:
			en = CASHIER_LOG;
			break;
		case 15:
			en = BUSAREA_ACTIVITY_TYPE;
			break;
		case 16:
			en = MSG_CENTER_MSG;
			break;
		case 20:
		    en = SHOP_FEEDBACK;
	          
		    break;
		default:
			throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_BIZTYPE);
			
		}
		return en.getName();
	}
	

    private BizTypeEnum(String name,Integer value){
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
