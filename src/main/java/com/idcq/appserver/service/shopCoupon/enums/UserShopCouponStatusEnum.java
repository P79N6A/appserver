package com.idcq.appserver.service.shopCoupon.enums;

/**
 * 优惠券使用状态枚举
 * 
 * @author ChenYongxin
 *
 */
public enum UserShopCouponStatusEnum {
	//使用状态：未使用=0,已使用=1
	NOT_IS_USE("未使用",0),
	IS_USE("已使用",1);
	
	private String name;
	private Integer value;
	
	private UserShopCouponStatusEnum(String name,Integer value){
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
	
	public static UserShopCouponStatusEnum valueOf(Integer value) {
		
		UserShopCouponStatusEnum userShopCouponStatusEnum = null;
		
		for (UserShopCouponStatusEnum e :UserShopCouponStatusEnum.values()) {
			if (e.getValue().equals(value)) {
				userShopCouponStatusEnum = e;
				break;
			}
		}
		
		return userShopCouponStatusEnum;
	}
	
}
