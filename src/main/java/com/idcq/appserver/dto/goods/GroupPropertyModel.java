package com.idcq.appserver.dto.goods;

import java.io.Serializable;

public class GroupPropertyModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4079651076561985745L;

/*	operateType	int	0	否	操作类型： 0-新增，1-修改
	为1时，groupPropertyId必填
	groupPropertyId	int		条件	商品族属性ID
	groupPropertyName	String		是	商品族属性名称
	proValuesId	int		条件	商品族属性值ID
	proValuesName	String		是	商品族属性值名称
	storageTotalNumber	double		否	库存量
	alarmNumberMax	double		否	库存告警最大值
	alarmNumberMin	double		否	库存告警最小值
	standardPrice	Double		是	目录价
	goodsNo	String		否	商品编号/货号
	barcode	String		否	条码号*/
	
	private Integer operateType;
	private Long groupPropertyId;
	private String groupPropertyName;
	private String shopPropertyIds;
	public String getShopPropertyIds() {
		return shopPropertyIds;
	}
	public void setShopPropertyIds(String shopPropertyIds) {
		this.shopPropertyIds = shopPropertyIds;
	}
	private Long proValuesId;
	private String proValuesName;
	private Double storageTotalNumber;
	private Double alarmNumberMax;
	private Double alarmNumberMin;
	private Double standardPrice;
	private String	goodsNo;
	private String	barcode;
	private String	plu;
	private Long goodsId;
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public String getPlu() {
		return plu;
	}
	public void setPlu(String plu) {
		this.plu = plu;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public String getProValuesName() {
		return proValuesName;
	}
	public void setProValuesName(String proValuesName) {
		this.proValuesName = proValuesName;
	}
	public Double getStorageTotalNumber() {
		return storageTotalNumber;
	}
	public void setStorageTotalNumber(Double storageTotalNumber) {
		this.storageTotalNumber = storageTotalNumber;
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
	public Double getStandardPrice() {
		return standardPrice;
	}
	public void setStandardPrice(Double standardPrice) {
		this.standardPrice = standardPrice;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Long getGroupPropertyId() {
		return groupPropertyId;
	}
	public void setGroupPropertyId(Long groupPropertyId) {
		this.groupPropertyId = groupPropertyId;
	}
	public String getGroupPropertyName() {
		return groupPropertyName;
	}
	public void setGroupPropertyName(String groupPropertyName) {
		this.groupPropertyName = groupPropertyName;
	}
	public Long getProValuesId() {
		return proValuesId;
	}
	public void setProValuesId(Long proValuesId) {
		this.proValuesId = proValuesId;
	}
	
}
