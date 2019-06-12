/**
 * Copyright (C) 2015 Asiainfo-Linkage
 * 
 * 会员积分实体类
 * @className:com.idcq.appserver.dto.user.ShopUserVantages
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

public class ShopUserVantages implements Serializable{

	/*	CREATE TABLE `1dcq_shop_user_vantages` (
	  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
	  `shop_id` int(10) unsigned NOT NULL COMMENT '商铺ID',
	  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户ID',
	  `vantages` float(8,2) unsigned DEFAULT NULL COMMENT '积分',
	  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
	  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
	  PRIMARY KEY (`id`) USING BTREE
	) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='商铺会员积分表';
*/
	private static final long serialVersionUID = -5067961785018820196L;
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
	 * 积分
	 */
	private Double vantages;
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
	public Double getVantages() {
		return vantages;
	}
	public void setVantages(Double vantages) {
		this.vantages = vantages;
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
