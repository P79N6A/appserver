/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.storage.GoodsListDto
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

public class StorageGoodsDto implements Serializable {

  /*	
    goodsId	string		否	商品ID，没有传值需要新增商品
	storagePrice	double		否	商品入库单价
	storageNumber	double		是	出/入库商品数量
	goodsTotalPrice	double		否	商品入库总价
	storageBillRemark	string		否	备注
	goodsName	string		是	商品名称
	goodsNo	string		否	货号/编号
	specsDesc	string		否	规格
	barcode	string		否	商品条码 
	unitName	string		否	单位，新增时没传默认为“个”
	standardPrice	double		条件	商品目录单价，新增是必填
	goodsCategoryId	int		否	商品分类id
	digitScale	int	0	否	单位支持的小数点位数
	alarmNumberMax	double		否	库存告警最大值
	alarmNumberMin	double		否	库存告警最小值
	
	*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -346216675417619147L;
	private Long goodsId;
	private Double storagePrice;
	private Double storageNumber;
	private Double goodsTotalPrice;
	private String storageBillRemark;
	private String goodsName;
	private String goodsNo;
	private String specsDesc;
	private String barcode;
	private Long unitId;
	private String unitName;
	private Double standardPrice;
	private Long goodsCategoryId;
	private Integer digitScale;
	private Double alarmNumberMax;
	private Double alarmNumberMin;
	private Integer changeType;
	
	
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
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
	public Double getStorageNumber() {
		return storageNumber;
	}
	public void setStorageNumber(Double storageNumber) {
		this.storageNumber = storageNumber;
	}
	public Double getGoodsTotalPrice() {
		return goodsTotalPrice;
	}
	public void setGoodsTotalPrice(Double goodsTotalPrice) {
		this.goodsTotalPrice = goodsTotalPrice;
	}
	public String getStorageBillRemark() {
		return storageBillRemark;
	}
	public void setStorageBillRemark(String storageBillRemark) {
		this.storageBillRemark = storageBillRemark;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getSpecsDesc() {
		return specsDesc;
	}
	public void setSpecsDesc(String specsDesc) {
		this.specsDesc = specsDesc;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Double getStandardPrice() {
		return standardPrice;
	}
	public void setStandardPrice(Double standardPrice) {
		this.standardPrice = standardPrice;
	}
	public Long getGoodsCategoryId() {
		return goodsCategoryId;
	}
	public void setGoodsCategoryId(Long goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}
	public Integer getDigitScale() {
		return digitScale;
	}
	public void setDigitScale(Integer digitScale) {
		this.digitScale = digitScale;
	}
	public Double getAlarmNumberMax() {
		return alarmNumberMax;
	}
	public void setAlarmNumberMax(Double alarmNumberMax) {
		this.alarmNumberMax = alarmNumberMax;
	}
	public Double getAlarmNumberMin() {
		return alarmNumberMin;
	}
	public void setAlarmNumberMin(Double alarmNumberMin) {
		this.alarmNumberMin = alarmNumberMin;
	}
	public Integer getChangeType() {
		return changeType;
	}
	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}

	
}	
