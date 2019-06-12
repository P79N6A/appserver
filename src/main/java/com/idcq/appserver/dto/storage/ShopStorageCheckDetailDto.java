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
public class ShopStorageCheckDetailDto implements Serializable {
	
	private static final long serialVersionUID = -5592846013694724789L;
	
	/**
	 * 详情ID
	 */
	private Long checkDetailId;
	/**
	 * 盘点ID
	 */
	private Long storageCheckId;
	/**
	 * 出/入库单号,销售出库时填写订单号
	 */
	private String storageCheckNo;
	/**
	 * 商铺ID
	 */
	private Long shopId;
	/**
	 * 商品ID
	 */
	private Long goodsId;
	/**
	 * 单价
	 */
	private Double storagePrice;
	/**
	 * 账面总量
	 */
	private Double goodsTotalNum;
	/**
	 * 账面总价
	 */
	private Double goodsTotalPrice;
	/**
	 * 盘点数量
	 */
	private Double checkNum;
	/**
	 * 盘点总价
	 */
	private Double checkTotalPrice;
	/**
	 * 盘点差额，>0盘盈，<0盘亏
	 */
	private Double differenceNum;
	/**
	 * 备注
	 */
	private String storageCheckRemark;
	/**
	 * 盘点类型:盘盈=1,盘亏=-1,盘准=0（数目符合）
	 */
	private Integer storageCheckType;
	/**
	 * 创建时间
	 */
	private Date createTime;
	public Long getCheckDetailId() {
		return checkDetailId;
	}
	public void setCheckDetailId(Long checkDetailId) {
		this.checkDetailId = checkDetailId;
	}
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
	public Double getStoragePrice() {
		return storagePrice;
	}
	public void setStoragePrice(Double storagePrice) {
		this.storagePrice = storagePrice;
	}
	public Double getGoodsTotalNum() {
		return goodsTotalNum;
	}
	public void setGoodsTotalNum(Double goodsTotalNum) {
		this.goodsTotalNum = goodsTotalNum;
	}
	public Double getGoodsTotalPrice() {
		return goodsTotalPrice;
	}
	public void setGoodsTotalPrice(Double goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}
	public Double getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(Double checkNum) {
		this.checkNum = checkNum;
	}
	public Double getCheckTotalPrice() {
		return checkTotalPrice;
	}
	public void setCheckTotalPrice(Double checkTotalPrice) {
		this.checkTotalPrice = checkTotalPrice;
	}
	public Double getDifferenceNum() {
		return differenceNum;
	}
	public void setDifferenceNum(Double differenceNum) {
		this.differenceNum = differenceNum;
	}
	public String getStorageCheckRemark() {
		return storageCheckRemark;
	}
	public void setStorageCheckRemark(String storageCheckRemark) {
		this.storageCheckRemark = storageCheckRemark;
	}
	public Integer getStorageCheckType() {
		return storageCheckType;
	}
	public void setStorageCheckType(Integer storageCheckType) {
		this.storageCheckType = storageCheckType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	

}
