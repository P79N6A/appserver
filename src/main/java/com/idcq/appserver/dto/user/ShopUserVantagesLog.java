/**
 * Copyright (C) 2015 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.user.ShopUserVantagesLog
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2015年9月28日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.user;

import java.io.Serializable;
import java.util.Date;

public class ShopUserVantagesLog implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 3963838064513755818L;
	/*	CREATE TABLE `1dcq_shop_user_vantages_log` (
			  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
			  `shop_id` int(10) unsigned NOT NULL COMMENT '商铺ID',
			  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户ID',
			  `order_id` varchar(32) NOT NULL DEFAULT '' COMMENT '订单编号',
			  `exchange_money` decimal(10,2) DEFAULT '0.00' COMMENT '兑换金额',
			  `money_to_vantages` float DEFAULT '0' COMMENT '每1元可兑换多少积分',
			  `exchange_vantages` float(8,2) unsigned DEFAULT NULL COMMENT '可兑换积分',
			  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
			  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
			  PRIMARY KEY (`id`) USING BTREE
			) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商铺会员积分日志表';*/
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 商铺id
	 */
	private Long shopId;
	/**
	 * 用户主键
	 */
	private Long userId;
	/**
	 * 订单id
	 */
	private String orderId;
	/**
	 * 兑换金额
	 */
	private Double exchangeMoney;
	/**
	 * 每1元可兑换多少积分
	 */
	private Double moneyToVantages;
	/**
	 * 可兑换积分
	 */
	private Double exchangeVantages;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Double getExchangeMoney() {
		return exchangeMoney;
	}
	public void setExchangeMoney(Double exchangeMoney) {
		this.exchangeMoney = exchangeMoney;
	}
	public Double getMoneyToVantages() {
		return moneyToVantages;
	}
	public void setMoneyToVantages(Double moneyToVantages) {
		this.moneyToVantages = moneyToVantages;
	}
	public Double getExchangeVantages() {
		return exchangeVantages;
	}
	public void setExchangeVantages(Double exchangeVantages) {
		this.exchangeVantages = exchangeVantages;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	
}
