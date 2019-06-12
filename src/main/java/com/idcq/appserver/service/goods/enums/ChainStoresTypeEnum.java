package com.idcq.appserver.service.goods.enums;

/**
 * 连锁店铺类型
 * 
 * @author ChenYongxin
 *
 */
public enum ChainStoresTypeEnum {
	//连锁店铺类型：0=非连锁,1=直营总部,2= 直营分店
	NOT_CHAIN_STORES("非连锁",0),
	HEADQUARTERS("直系总部",1),
	IS_CHAIN_STORES("直营分店",2);
	
	private String name;
	private Integer value;
	
	private ChainStoresTypeEnum(String name,Integer value){
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
	public static ChainStoresTypeEnum valueOf(Integer value) {
		
		ChainStoresTypeEnum chainStoresTypeEnum = null;
		
		for (ChainStoresTypeEnum e :ChainStoresTypeEnum.values()) {
			if (e.getValue().equals(value)) {
				chainStoresTypeEnum = e;
				break;
			}
		}
		
		return chainStoresTypeEnum;
	}	
	
}
