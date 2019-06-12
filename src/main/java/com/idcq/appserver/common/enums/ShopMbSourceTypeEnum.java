package com.idcq.appserver.common.enums;


/**
 * 店内会员来源类型枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:36:45
 */
public enum ShopMbSourceTypeEnum {
	
	SHOP_IMPORT("店内导入",1),
	PAGE_ADD("界面新增",2),
	WX_SCAN("店内导入",3);
	
    private String name;
    private Integer value;
    
    private ShopMbSourceTypeEnum(String name,Integer value){
        this.name = name;
        this.value = value;
    }
    
    public String getName(){
        return this.name;
    }
    
    public Integer getValue(){
    	return this.value;
    }

}
