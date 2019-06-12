/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.storage.ShopStorageCheckNoteDto
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年4月13日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.storage;

import java.io.Serializable;
import java.util.Date;

/**
 * 盘点记录实体
 * 
 * @author ChenYongxin
 *
 */
public class ShopStorageCheckNoteDto implements Serializable {
     /**
	 * 
	 */
	private static final long serialVersionUID = -5592846013694724789L;
	/*	 
	  `check_detail_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '盘点详单ID',
	  `storage_check_id` int(10) unsigned NOT NULL COMMENT '盘点ID',
	  `goods_id` int(10) unsigned NOT NULL COMMENT '商品ID',
	  `shop_id` int(10) unsigned DEFAULT NULL COMMENT '商铺ID',
	  `storage_price` decimal(12,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '单价',
	  `goods_total_num` decimal(12,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '账面总量',
	  `goods_total_price` decimal(12,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '账面总价',
	  `check_num` decimal(12,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '盘点数量',
	  `check_total_price` decimal(12,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '盘点总价',
	  `storage_check_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '盘点类型:盘盈=1,盘亏=-1,盘准=0（数目符合）',
	  `difference_num` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '盘点差额，>0盘盈，<0盘亏',
	  `storage_check_remark` varchar(50) DEFAULT NULL COMMENT '备注',
	  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
	  */
	/**
	 * 出入库ID
	 */
	private Long storageCheckId;
	/**
	 * 出/入库单号,销售出库时填写订单号
	 */
	private String storageCheckNo;

	/**
	 * 账面总价
	 */
	private Double goodsTotalPrice;
	/**
	 * 商铺ID
	 */
	private Long shopId;
	/**
	 * 经手人ID（店铺管理者=0）
	 */
	private Long operaterId;
	/**
	 * 经手人
	 */
	private String operaterName;
	/**
	 * 备注
	 */
	private String storageCheckRemark;
	/**
	 * 出/入库时间
	 */
	private Date storageCheckTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	public Long getStorageCheckId() {
		return storageCheckId;
	}
	public void setStorageCheckId(Long storageCheckId) {
		this.storageCheckId = storageCheckId;
	}
	public String getStorageCheckNo() {
		return storageCheckNo;
	}
	public void setStorageCheckNo(String storageCheckNo) {
		this.storageCheckNo = storageCheckNo;
	}
	public Double getGoodsTotalPrice() {
		return goodsTotalPrice;
	}
	public void setGoodsTotalPrice(Double goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getOperaterId() {
		return operaterId;
	}
	public void setOperaterId(Long operaterId) {
		this.operaterId = operaterId;
	}
	public String getOperaterName() {
		return operaterName;
	}
	public void setOperaterName(String operaterName) {
		this.operaterName = operaterName;
	}
	public String getStorageCheckRemark() {
		return storageCheckRemark;
	}
	public void setStorageCheckRemark(String storageCheckRemark) {
		this.storageCheckRemark = storageCheckRemark;
	}
	public Date getStorageCheckTime() {
		return storageCheckTime;
	}
	public void setStorageCheckTime(Date storageCheckTime) {
		this.storageCheckTime = storageCheckTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
