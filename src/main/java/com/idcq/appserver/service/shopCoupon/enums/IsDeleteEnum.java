package com.idcq.appserver.service.shopCoupon.enums;

/**
 * 优惠券是否删除
 * 
 * @author ChenYongxin
 *
 */
public enum IsDeleteEnum {
	//'是否删除：是-1，否-0'
	IS_DELETE("是",1),
	NOT_DELETE("否",0);
	
	private String name;
	private Integer value;
	
	private IsDeleteEnum(String name,Integer value){
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

	public static IsDeleteEnum valueOf(Integer value) {
		
		IsDeleteEnum isDeleteEnum = null;
		
		for (IsDeleteEnum e :IsDeleteEnum.values()) {
			if (e.getValue().equals(value)) {
				isDeleteEnum = e;
				break;
			}
		}
		
		return isDeleteEnum;
	}
	
}
