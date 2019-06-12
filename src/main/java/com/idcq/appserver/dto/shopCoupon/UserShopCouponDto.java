package com.idcq.appserver.dto.shopCoupon;

import java.io.Serializable;
import java.util.Date;

public class UserShopCouponDto  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 275170142473002267L;
	
	
	/**
	 * 用户持有店铺优惠券适用的商品分类id
	 */
	private Long goodsCategoryId;
	public Long getGoodsCategoryId() {
		return goodsCategoryId;
	}
	public void setGoodsCategoryId(Long goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}
	/**
	 * 用户持有店铺优惠券id
	 */
	private Integer userShopCouponId;
	
	/**
	 * 店内会员id
	 */
	private Integer shopMemeberId;
	
	/**
	 * 店铺id
	 */
	private Long shopId;
	
	/**
	 * 会员id
	 */
	private Long userId;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 店内优惠券id
	 */
	private Integer shopCouponId;
	
	/**
	 * 使用状态：未使用=0,已使用=1
	 */
	private Integer couponStatus;
	
	/**
	 * 有效期开始日期
	 */
	private Date beginDate;
	
	/**
	 * 有效期结束日期
	 */
	private Date endDate;
	
	/**
	 * 店内优惠券序号 用于区分多张同一优惠券
	 */
	private Integer shopCouponIndex;
	
	/**
	 * 领取优惠券时间
	 */
	private Date getCouponTime;
	
	
	/**
	 * 使用优惠券时间
	 */
	private Date usedCouponTime;
	
	private Double couponUsedCondition;
	/**
	 * 订单的唯一标识
	 */
	private String orderId;
	
	private Integer pageNo = 0;
	
	private Integer pageSize = 10;

	public Double getCouponUsedCondition() {
		return couponUsedCondition;
	}
	public void setCouponUsedCondition(Double couponUsedCondition) {
		this.couponUsedCondition = couponUsedCondition;
	}

	public Integer getUserShopCouponId() {
		return userShopCouponId;
	}
	public void setUserShopCouponId(Integer userShopCouponId) {
		this.userShopCouponId = userShopCouponId;
	}
	public Integer getShopMemeberId() {
		return shopMemeberId;
	}
	public void setShopMemeberId(Integer shopMemeberId) {
		this.shopMemeberId = shopMemeberId;
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getShopCouponId() {
		return shopCouponId;
	}
	public void setShopCouponId(Integer shopCouponId) {
		this.shopCouponId = shopCouponId;
	}
	public Integer getCouponStatus() {
		return couponStatus;
	}
	public void setCouponStatus(Integer couponStatus) {
		this.couponStatus = couponStatus;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getShopCouponIndex() {
		return shopCouponIndex;
	}
	public void setShopCouponIndex(Integer shopCouponIndex) {
		this.shopCouponIndex = shopCouponIndex;
	}

	public Date getGetCouponTime() {
		return getCouponTime;
	}
	public void setGetCouponTime(Date getCouponTime) {
		this.getCouponTime = getCouponTime;
	}
	public Date getUsedCouponTime() {
		return usedCouponTime;
	}
	public void setUsedCouponTime(Date usedCouponTime) {
		this.usedCouponTime = usedCouponTime;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
	
	
}
