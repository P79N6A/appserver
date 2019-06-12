package com.idcq.appserver.common.enums;


/**
 * 店内会员状态枚举
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日	
 * @time 上午8:21:20
 */
public enum ShopMbStatusEnum {
	
	STOP("停用",0),
	NORMAL("正常",1);
	
    private String name;
    private Integer value;
    
    private ShopMbStatusEnum(String name,Integer value){
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
