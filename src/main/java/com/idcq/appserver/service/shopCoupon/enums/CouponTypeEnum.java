package com.idcq.appserver.service.shopCoupon.enums;

/**
 * 优惠券类型
 * 
 * @author ChenYongxin
 *
 */
public enum CouponTypeEnum {
	//'优惠券适用类型：全部商品-1，指定商品类别-2',
	ALL_GOODS("全部商品",1),
	APPOINT_DOODS("指定商品",2);
	
	private String name;
	private Integer value;
	
	private CouponTypeEnum(String name,Integer value){
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
	
	public static CouponTypeEnum valueOf(Integer value) {
		
		CouponTypeEnum couponTypeEnum = null;
		
		for (CouponTypeEnum e :CouponTypeEnum.values()) {
			if (e.getValue().equals(value)) {
				couponTypeEnum = e;
				break;
			}
		}
		
		return couponTypeEnum;
	}	
}
