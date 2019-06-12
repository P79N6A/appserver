/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.storage.ShopStorageNote
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年4月6日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.storage;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author ChenYongxin
 * 
 */
public class ShopStorageDetailDto implements Serializable {

	private static final long serialVersionUID = -7543620419877806626L;
	
	/**
	 * 详单ID
	 */
	private Long storageDetailId;
	/**
	 * 业务ID：biz_type=17时存出/入库ID，=18时存盘点ID
	 */
	private Long bizId;
	/**
	 * 业务主键类型:出入库记录=17,盘点记录=18',
	 */
	private Integer bizType;
	/**
	 * '变动类型:盘亏=-1,盘盈=1,进货入库=11,其他入库=12,销售出库=13,其他出库=14',
	 */
	private Integer changeType;
	/**
	 * 商品ID
	 */
	private Long goodsId;
	/**
	 * 入库单价
	 */
	private Double storagePrice;
	/**
	 * 总价
	 */
	private Double goodsTotalPrice;
	/**
	 * 出入库商品数量
	 */
	private Double storageNumber;
	/**
	 * 出入库商品后数量
	 */
	private Double storageAfterNumber;
	/**
	 * 备注
	 */
	private String detailRemark;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 单位id
	 */
	private Long unitId;
	/**
	 * 单位名称
	 */
	private String unitName;
	/**
	 * 小数点位数
	 */
	private Integer digitScale;
	
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Integer getDigitScale() {
		return digitScale;
	}
	public void setDigitScale(Integer digitScale) {
		this.digitScale = digitScale;
	}
	public Long getStorageDetailId() {
		return storageDetailId;
	}
	public void setStorageDetailId(Long storageDetailId) {
		this.storageDetailId = storageDetailId;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public Integer getBizType() {
		return bizType;
	}
	public void setBizType(Integer bizType) {
		this.bizType = bizType;
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
	public Double getGoodsTotalPrice() {
		return goodsTotalPrice;
	}
	public void setGoodsTotalPrice(Double goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}
	public Double getStorageNumber() {
		return storageNumber;
	}
	public void setStorageNumber(Double storageNumber) {
		this.storageNumber = storageNumber;
	}
	public Double getStorageAfterNumber() {
		return storageAfterNumber;
	}
	public void setStorageAfterNumber(Double storageAfterNumber) {
		this.storageAfterNumber = storageAfterNumber;
	}
	public String getDetailRemark() {
		return detailRemark;
	}
	public void setDetailRemark(String detailRemark) {
		this.detailRemark = detailRemark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getChangeType() {
		return changeType;
	}
	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
	

}
