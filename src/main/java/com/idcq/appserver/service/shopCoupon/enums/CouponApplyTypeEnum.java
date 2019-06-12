package com.idcq.appserver.service.shopCoupon.enums;

/**
 * 优惠券适用类型
 * 
 * @author ChenYongxin
 *
 */
public enum CouponApplyTypeEnum {
	//优惠券适用类型：商品分类-1，商品=2
	GOODS_CATEGORY("商品分类",1),
	DOODS("商品",2);
	
	private String name;
	private Integer value;
	
	private CouponApplyTypeEnum(String name,Integer value){
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
	
	public static CouponApplyTypeEnum valueOf(Integer value) {
		
		CouponApplyTypeEnum couponApplyTypeEnum = null;
		
		for (CouponApplyTypeEnum e :CouponApplyTypeEnum.values()) {
			if (e.getValue().equals(value)) {
				couponApplyTypeEnum = e;
				break;
			}
		}
		
		return couponApplyTypeEnum;
	}
	
}
