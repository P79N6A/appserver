package com.idcq.appserver.dto.pay;

import java.io.Serializable;
import java.util.Date;

public class OrderGoodsSettle implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.ogs_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long ogsId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.order_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private String orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.user_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.order_time
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Date orderTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.order_goods_price_before_discount
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double orderGoodsPriceBeforeDiscount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.order_goods_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double orderGoodsPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.order_logistics_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double orderLogisticsPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.order_total_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double orderTotalPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.order_settle_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double orderSettlePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.order_goods_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long orderGoodsId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long shopId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.goods_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long goodsId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.goods_number
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double goodsNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.goods_settle_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double goodsStandardPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_income_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double shopIncomePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_income_ratio
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double shopIncomeRatio;
    private Integer shopIncomeSettleFlag;
    
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.platform_total_income_ratio
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double shopMemberDiscount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.platform_total_income_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double platformTotalIncomePrice;
    
    private Double platformTotalIncomeRatio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.platform_net_income_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double platformNetIncomePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.user_share_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double userSharePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.user_share_ratio
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double userShareRatio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.user_share_settle_flag
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Integer userShareSettleFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.user_ref_share_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double userRefSharePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.user_ref_share_ratio
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double userRefShareRatio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.user_ref_share_settle_flag
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Integer userRefShareSettleFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.user_ref_share_user_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long userRefShareUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.user_ref_share_user_type
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Integer userRefShareUserType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_ref_share_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double shopRefSharePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_ref_share_ratio
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double shopRefShareRatio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_ref_share_settle_flag
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Integer shopRefShareSettleFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_ref_share_user_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long shopRefShareUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_ref_share_user_type
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Integer shopRefShareUserType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_serve_share_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double shopServeSharePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_serve_share_ratio
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double shopServeShareRatio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_serve_share_settle_flag
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Integer shopServeShareSettleFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.shop_serve_share_user_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long shopServeShareUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level1_agent_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double level1AgentPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level1_agent_share_ratio
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double level1AgentShareRatio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level1_agent_share_settle_flag
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Integer level1AgentShareSettleFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level1_agent_share_user_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long level1AgentShareUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.city_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long cityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level2_agent_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double level2AgentPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level2_agent_share_ratio
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double level2AgentShareRatio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level2_agent_share_settle_flag
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Integer level2AgentShareSettleFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level2_agent_share_user_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long level2AgentShareUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.district_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Integer districtId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level3_agent_price
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double level3AgentPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level3_agent_share_ratio
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Double level3AgentShareRatio;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level3_agent_share_settle_flag
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Integer level3AgentShareSettleFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.level3_agent_share_user_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long level3AgentShareUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.town_id
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Long townId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_order_goods_settle.create_time
     *
     * @mbggenerated Tue Apr 07 15:01:19 CST 2015
     */
    private Date createTime;
    /**
     * 商品结算天数
     */
    private Integer shopSettleDelayDays;
    /**
     * 用户结算天数
     */
    private Integer userSettleDelayDays;
    
    private Double operatorsPrice;
    
    /* 20160504添加连锁店 */
    private Double integrationPromotionUserPrice; // 连锁店铺整合推广人收入
    private Long integrationPromotionUserId; // 连锁店铺整合推广人id
    private Double integrationPromotionRatio; // 连锁店铺整合推广人分成比例
    
    private Double integrationfacilitateUserPrice; // 连锁店铺整合促成人收入
    private Long integrationFacilitateUserId; // 连锁店铺整合促成人id
    private Double integrationFacilitateRatio; // 连锁店铺整合推广人分成比例
    
    private Double integrationFrincipalUserPrice; // 连锁店铺店主收入
    private Long integrationFrincipalUserId; // 连锁店铺店主id
    private Double integrationPrincipalRatio; // 连锁店铺整合推广人分成比例


	public Integer getShopSettleDelayDays() {
		return shopSettleDelayDays;
	}

	public void setShopSettleDelayDays(Integer shopSettleDelayDays) {
		this.shopSettleDelayDays = shopSettleDelayDays;
	}

	public Integer getUserSettleDelayDays() {
		return userSettleDelayDays;
	}

	public void setUserSettleDelayDays(Integer userSettleDelayDays) {
		this.userSettleDelayDays = userSettleDelayDays;
	}

	public Long getOgsId() {
		return ogsId;
	}

	public void setOgsId(Long ogsId) {
		this.ogsId = ogsId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Double getOrderGoodsPriceBeforeDiscount() {
		return orderGoodsPriceBeforeDiscount;
	}

	public void setOrderGoodsPriceBeforeDiscount(
			Double orderGoodsPriceBeforeDiscount) {
		this.orderGoodsPriceBeforeDiscount = orderGoodsPriceBeforeDiscount;
	}

	public Double getOrderGoodsPrice() {
		return orderGoodsPrice;
	}

	public void setOrderGoodsPrice(Double orderGoodsPrice) {
		this.orderGoodsPrice = orderGoodsPrice;
	}

	public Double getOrderLogisticsPrice() {
		return orderLogisticsPrice;
	}

	public void setOrderLogisticsPrice(Double orderLogisticsPrice) {
		this.orderLogisticsPrice = orderLogisticsPrice;
	}

	public Double getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	public Double getOrderSettlePrice() {
		return orderSettlePrice;
	}

	public void setOrderSettlePrice(Double orderSettlePrice) {
		this.orderSettlePrice = orderSettlePrice;
	}

	public Long getOrderGoodsId() {
		return orderGoodsId;
	}

	public void setOrderGoodsId(Long orderGoodsId) {
		this.orderGoodsId = orderGoodsId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Double getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Double goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public Double getShopIncomePrice() {
		return shopIncomePrice;
	}

	public void setShopIncomePrice(Double shopIncomePrice) {
		this.shopIncomePrice = shopIncomePrice;
	}

	public Double getShopIncomeRatio() {
		return shopIncomeRatio;
	}

	public void setShopIncomeRatio(Double shopIncomeRatio) {
		this.shopIncomeRatio = shopIncomeRatio;
	}

	public Double getGoodsStandardPrice() {
		return goodsStandardPrice;
	}

	public void setGoodsStandardPrice(Double goodsStandardPrice) {
		this.goodsStandardPrice = goodsStandardPrice;
	}

	public Double getShopMemberDiscount() {
		return shopMemberDiscount;
	}

	public void setShopMemberDiscount(Double shopMemberDiscount) {
		this.shopMemberDiscount = shopMemberDiscount;
	}

	public Double getPlatformTotalIncomePrice() {
		return platformTotalIncomePrice;
	}

	public void setPlatformTotalIncomePrice(Double platformTotalIncomePrice) {
		this.platformTotalIncomePrice = platformTotalIncomePrice;
	}

	public Double getPlatformNetIncomePrice() {
		return platformNetIncomePrice;
	}

	public void setPlatformNetIncomePrice(Double platformNetIncomePrice) {
		this.platformNetIncomePrice = platformNetIncomePrice;
	}

	public Double getUserSharePrice() {
		return userSharePrice;
	}

	public void setUserSharePrice(Double userSharePrice) {
		this.userSharePrice = userSharePrice;
	}

	public Double getUserShareRatio() {
		return userShareRatio;
	}

	public void setUserShareRatio(Double userShareRatio) {
		this.userShareRatio = userShareRatio;
	}

	public Integer getUserShareSettleFlag() {
		return userShareSettleFlag;
	}

	public void setUserShareSettleFlag(Integer userShareSettleFlag) {
		this.userShareSettleFlag = userShareSettleFlag;
	}

	public Double getUserRefSharePrice() {
		return userRefSharePrice;
	}

	public void setUserRefSharePrice(Double userRefSharePrice) {
		this.userRefSharePrice = userRefSharePrice;
	}

	public Double getUserRefShareRatio() {
		return userRefShareRatio;
	}

	public void setUserRefShareRatio(Double userRefShareRatio) {
		this.userRefShareRatio = userRefShareRatio;
	}

	public Integer getUserRefShareSettleFlag() {
		return userRefShareSettleFlag;
	}

	public void setUserRefShareSettleFlag(Integer userRefShareSettleFlag) {
		this.userRefShareSettleFlag = userRefShareSettleFlag;
	}

	public Long getUserRefShareUserId() {
		return userRefShareUserId;
	}

	public void setUserRefShareUserId(Long userRefShareUserId) {
		this.userRefShareUserId = userRefShareUserId;
	}

	public Integer getUserRefShareUserType() {
		return userRefShareUserType;
	}

	public void setUserRefShareUserType(Integer userRefShareUserType) {
		this.userRefShareUserType = userRefShareUserType;
	}

	public Double getShopRefSharePrice() {
		return shopRefSharePrice;
	}

	public void setShopRefSharePrice(Double shopRefSharePrice) {
		this.shopRefSharePrice = shopRefSharePrice;
	}

	public Double getShopRefShareRatio() {
		return shopRefShareRatio;
	}

	public void setShopRefShareRatio(Double shopRefShareRatio) {
		this.shopRefShareRatio = shopRefShareRatio;
	}

	public Integer getShopRefShareSettleFlag() {
		return shopRefShareSettleFlag;
	}

	public void setShopRefShareSettleFlag(Integer shopRefShareSettleFlag) {
		this.shopRefShareSettleFlag = shopRefShareSettleFlag;
	}

	public Long getShopRefShareUserId() {
		return shopRefShareUserId;
	}

	public void setShopRefShareUserId(Long shopRefShareUserId) {
		this.shopRefShareUserId = shopRefShareUserId;
	}

	public Integer getShopRefShareUserType() {
		return shopRefShareUserType;
	}

	public void setShopRefShareUserType(Integer shopRefShareUserType) {
		this.shopRefShareUserType = shopRefShareUserType;
	}

	public Double getShopServeSharePrice() {
		return shopServeSharePrice;
	}

	public void setShopServeSharePrice(Double shopServeSharePrice) {
		this.shopServeSharePrice = shopServeSharePrice;
	}

	public Double getShopServeShareRatio() {
		return shopServeShareRatio;
	}

	public void setShopServeShareRatio(Double shopServeShareRatio) {
		this.shopServeShareRatio = shopServeShareRatio;
	}

	public Integer getShopServeShareSettleFlag() {
		return shopServeShareSettleFlag;
	}

	public void setShopServeShareSettleFlag(Integer shopServeShareSettleFlag) {
		this.shopServeShareSettleFlag = shopServeShareSettleFlag;
	}

	public Long getShopServeShareUserId() {
		return shopServeShareUserId;
	}

	public void setShopServeShareUserId(Long shopServeShareUserId) {
		this.shopServeShareUserId = shopServeShareUserId;
	}

	public Double getLevel1AgentPrice() {
		return level1AgentPrice;
	}

	public void setLevel1AgentPrice(Double level1AgentPrice) {
		this.level1AgentPrice = level1AgentPrice;
	}

	public Double getLevel1AgentShareRatio() {
		return level1AgentShareRatio;
	}

	public void setLevel1AgentShareRatio(Double level1AgentShareRatio) {
		this.level1AgentShareRatio = level1AgentShareRatio;
	}

	public Integer getLevel1AgentShareSettleFlag() {
		return level1AgentShareSettleFlag;
	}

	public void setLevel1AgentShareSettleFlag(Integer level1AgentShareSettleFlag) {
		this.level1AgentShareSettleFlag = level1AgentShareSettleFlag;
	}

	public Long getLevel1AgentShareUserId() {
		return level1AgentShareUserId;
	}

	public void setLevel1AgentShareUserId(Long level1AgentShareUserId) {
		this.level1AgentShareUserId = level1AgentShareUserId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Double getLevel2AgentPrice() {
		return level2AgentPrice;
	}

	public void setLevel2AgentPrice(Double level2AgentPrice) {
		this.level2AgentPrice = level2AgentPrice;
	}

	public Double getLevel2AgentShareRatio() {
		return level2AgentShareRatio;
	}

	public void setLevel2AgentShareRatio(Double level2AgentShareRatio) {
		this.level2AgentShareRatio = level2AgentShareRatio;
	}

	public Integer getLevel2AgentShareSettleFlag() {
		return level2AgentShareSettleFlag;
	}

	public void setLevel2AgentShareSettleFlag(Integer level2AgentShareSettleFlag) {
		this.level2AgentShareSettleFlag = level2AgentShareSettleFlag;
	}

	public Long getLevel2AgentShareUserId() {
		return level2AgentShareUserId;
	}

	public void setLevel2AgentShareUserId(Long level2AgentShareUserId) {
		this.level2AgentShareUserId = level2AgentShareUserId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Double getLevel3AgentPrice() {
		return level3AgentPrice;
	}

	public void setLevel3AgentPrice(Double level3AgentPrice) {
		this.level3AgentPrice = level3AgentPrice;
	}

	public Double getLevel3AgentShareRatio() {
		return level3AgentShareRatio;
	}

	public void setLevel3AgentShareRatio(Double level3AgentShareRatio) {
		this.level3AgentShareRatio = level3AgentShareRatio;
	}

	public Integer getLevel3AgentShareSettleFlag() {
		return level3AgentShareSettleFlag;
	}

	public void setLevel3AgentShareSettleFlag(Integer level3AgentShareSettleFlag) {
		this.level3AgentShareSettleFlag = level3AgentShareSettleFlag;
	}

	public Long getLevel3AgentShareUserId() {
		return level3AgentShareUserId;
	}

	public void setLevel3AgentShareUserId(Long level3AgentShareUserId) {
		this.level3AgentShareUserId = level3AgentShareUserId;
	}

	public Long getTownId() {
		return townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getPlatformTotalIncomeRatio() {
		return platformTotalIncomeRatio;
	}

	public void setPlatformTotalIncomeRatio(Double platformTotalIncomeRatio) {
		this.platformTotalIncomeRatio = platformTotalIncomeRatio;
	}

	public Integer getShopIncomeSettleFlag() {
		return shopIncomeSettleFlag;
	}

	public void setShopIncomeSettleFlag(Integer shopIncomeSettleFlag) {
		this.shopIncomeSettleFlag = shopIncomeSettleFlag;
	}

    public Double getOperatorsPrice()
    {
        return operatorsPrice;
    }

    public void setOperatorsPrice(Double operatorsPrice)
    {
        this.operatorsPrice = operatorsPrice;
    }

    public Double getIntegrationPromotionUserPrice()
    {
        return integrationPromotionUserPrice;
    }

    public void setIntegrationPromotionUserPrice(Double integrationPromotionUserPrice)
    {
        this.integrationPromotionUserPrice = integrationPromotionUserPrice;
    }

    public Long getIntegrationPromotionUserId()
    {
        return integrationPromotionUserId;
    }

    public void setIntegrationPromotionUserId(Long integrationPromotionUserId)
    {
        this.integrationPromotionUserId = integrationPromotionUserId;
    }

    public Double getIntegrationfacilitateUserPrice()
    {
        return integrationfacilitateUserPrice;
    }

    public void setIntegrationfacilitateUserPrice(Double integrationfacilitateUserPrice)
    {
        this.integrationfacilitateUserPrice = integrationfacilitateUserPrice;
    }

    public Long getIntegrationFacilitateUserId()
    {
        return integrationFacilitateUserId;
    }

    public void setIntegrationFacilitateUserId(Long integrationFacilitateUserId)
    {
        this.integrationFacilitateUserId = integrationFacilitateUserId;
    }

    public Double getIntegrationFrincipalUserPrice()
    {
        return integrationFrincipalUserPrice;
    }

    public void setIntegrationFrincipalUserPrice(Double integrationFrincipalUserPrice)
    {
        this.integrationFrincipalUserPrice = integrationFrincipalUserPrice;
    }

    public Long getIntegrationFrincipalUserId()
    {
        return integrationFrincipalUserId;
    }

    public void setIntegrationFrincipalUserId(Long integrationFrincipalUserId)
    {
        this.integrationFrincipalUserId = integrationFrincipalUserId;
    }

    public Double getIntegrationPromotionRatio()
    {
        return integrationPromotionRatio;
    }

    public void setIntegrationPromotionRatio(Double integrationPromotionRatio)
    {
        this.integrationPromotionRatio = integrationPromotionRatio;
    }

    public Double getIntegrationFacilitateRatio()
    {
        return integrationFacilitateRatio;
    }

    public void setIntegrationFacilitateRatio(Double integrationFacilitateRatio)
    {
        this.integrationFacilitateRatio = integrationFacilitateRatio;
    }

    public Double getIntegrationPrincipalRatio()
    {
        return integrationPrincipalRatio;
    }

    public void setIntegrationPrincipalRatio(Double integrationPrincipalRatio)
    {
        this.integrationPrincipalRatio = integrationPrincipalRatio;
    }
	
    
}
