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
 * 出入库详单
 * 
 * @author ChenYongxin
 * 
 */
public class ShopStorageNoteDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3387922541884635350L;

	/**
	 * 出入库ID
	 */
	private Long storageId;
	/**
	 * 出/入库单号,销售出库时填写订单号
	 */
	private String storageNo;
	/**
	 * 出/入库类型:进货入库=1,其他入库=2,销售出库=3,其他出库=4
	 */
	private Integer storageType;

	/**
	 * 总价
	 */
	private Double totalPrice;
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
	private String storageRemark;
	/**
	 * 出/入库时间
	 */
	private Date storageTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 供应商
	 */
	private String vender;
	/**
	 * 近货人
	 */
	private String buyer;	
	
	public Long getStorageId() {
		return storageId;
	}
	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}
	public String getStorageNo() {
		return storageNo;
	}
	public void setStorageNo(String storageNo) {
		this.storageNo = storageNo;
	}
	public Integer getStorageType() {
		return storageType;
	}
	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
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
	public String getStorageRemark() {
		return storageRemark;
	}
	public void setStorageRemark(String storageRemark) {
		this.storageRemark = storageRemark;
	}
	public Date getStorageTime() {
		return storageTime;
	}
	public void setStorageTime(Date storageTime) {
		this.storageTime = storageTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getVender() {
		return vender;
	}
	public void setVender(String vender) {
		this.vender = vender;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

}
