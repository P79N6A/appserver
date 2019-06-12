package com.idcq.appserver.common.enums;
/**
 * 商圈活动店铺类型
 * @ClassName: ShopTypeEnum 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年3月18日 下午4:26:31 
 *
 */
public enum ShopTypeEnum {
	
	START("发起","1"),
	JOIN("参与","2");
	
    private String name;
    private String value;
    
    private ShopTypeEnum(String name,String value){
        this.name = name;
        this.value = value;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getValue(){
    	return this.value;
    }
}
