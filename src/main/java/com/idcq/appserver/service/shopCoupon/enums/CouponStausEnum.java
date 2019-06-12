package com.idcq.appserver.service.shopCoupon.enums;

/**
 * 优惠券使用状态枚举
 * 
 * @author ChenYongxin
 *
 */
public enum CouponStausEnum {
	//couponStaus tinyint(2) UNSIGNED NOT NULL DEFAULT 1 COMMENT '优惠券状态：启用-1,停用-0',
	ENABLE("启用",1),
	DISABLE("停用",0);
	
	private String name;
	private Integer value;
	
	private CouponStausEnum(String name,Integer value){
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
	public static CouponStausEnum valueOf(Integer value) {
		
		CouponStausEnum couponStausEnum = null;
		
		for (CouponStausEnum e :CouponStausEnum.values()) {
			if (e.getValue().equals(value)) {
				couponStausEnum = e;
				break;
			}
		}
		
		return couponStausEnum;
	}	
	
}
