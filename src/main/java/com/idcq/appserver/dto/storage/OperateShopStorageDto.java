/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.storage.OperateShopStorageDto
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
import java.util.List;

/**
 * PCS1：商铺商品出入库接口接受实体
 * 
 * @author ChenYongxin
 *
 */
public class OperateShopStorageDto implements Serializable {
   /**
	 * 
	 */
	private static final long serialVersionUID = 6337772807376784573L;
  /*	
		shopId	int		是	商铺ID
		token	string		条件	商铺令牌，收银机调用时必传
		storageType	int		是	操作类型。1=进货入库，2=其他入库，
		3=销售出库，4=其他出库
		storageNo	sting		否	出入库单号,没有传值时服务端默认使用如下规则填值
		出库=CK+时间戳 
		入库=RK+时间戳 
		销售出库时填写订单号
		totalPrice	double		否	整个出入库单总价
		operaterId	int		否	操作员ID，商铺老板传0
		operaterName	string		否	操作人
		storageTime	datetime		是	出入库时间
		storageRemark	string		否	备注
		vender	string		否	供应商
        buyer	string		否	采购员
		goodsList	list		否	商品list
	*/
	private Long shopId;
	private String token;
	private Integer  storageType;
	private String  storageNo;
	private Long  operaterId;
	private String  operaterName;
	private String  storageTime;
	private String  storageRemark;
	private Double totalPrice;
	private String vender;
	private String buyer;
	private Boolean  isUpdateGoods = true;//是否更新商品库存
	private List<StorageGoodsDto>  goodsList;
	
	
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	public String getStorageNo() {
		return storageNo;
	}
	public void setStorageNo(String storageNo) {
		this.storageNo = storageNo;
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
	public String getStorageTime() {
		return storageTime;
	}
	public void setStorageTime(String storageTime) {
		this.storageTime = storageTime;
	}
	public String getStorageRemark() {
		return storageRemark;
	}
	public void setStorageRemark(String storageRemark) {
		this.storageRemark = storageRemark;
	}
	public List<StorageGoodsDto> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<StorageGoodsDto> goodsList) {
		this.goodsList = goodsList;
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
	public Boolean getIsUpdateGoods() {
		return isUpdateGoods;
	}
	public void setIsUpdateGoods(Boolean isUpdateGoods) {
		this.isUpdateGoods = isUpdateGoods;
	}
	
	
}
