package com.idcq.appserver.service.shopCoupon.enums;

/**
 * 是否允许与其他优惠券共用
 * 
 * @author ChenYongxin
 *
 */
public enum IsUsedTogetherEnum {
	//'是否允许与其他优惠券共用：允许-1，不允许-0',
	ENABLE("允许",1),
	DISABLE("不允许",0);
	
	private String name;
	private Integer value;
	
	private IsUsedTogetherEnum(String name,Integer value){
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
	public static IsUsedTogetherEnum valueOf(Integer value) {
		
		IsUsedTogetherEnum isUsedTogetherEnum = null;
		
		for (IsUsedTogetherEnum e :IsUsedTogetherEnum.values()) {
			if (e.getValue().equals(value)) {
				isUsedTogetherEnum = e;
				break;
			}
		}
		
		return isUsedTogetherEnum;
	}	
	
}
