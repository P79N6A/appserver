package com.idcq.appserver.common.enums;
/**
 * 账户类型
 *
 */
public enum AccountTypeEnum {
	
	USER("用户",1),
	SHOP("商铺",2);
	
    private String name;
    private Integer value;
    
    private AccountTypeEnum(String name,Integer value){
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
