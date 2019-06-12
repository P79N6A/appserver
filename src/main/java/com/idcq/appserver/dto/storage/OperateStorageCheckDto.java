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
public class OperateStorageCheckDto implements Serializable {
   /**
	 * 
	 */
	private static final long serialVersionUID = 6337772807376784573L;
  /*	
		字段名	数据类型	默认值	必填项 	备注
		shopId	int		是	商铺ID
		token	string		条件	商铺令牌，收银机调用时必传
		checkNo	sting		否	盘点单号,没有传值时服务端默认使用如下规则填值
		出库=PD+商铺ID+时间戳 ，
		operaterId	int		是	操作员ID，商铺老板传0
		operaterName	string		是	操作人
		checkTime	datetime		是	盘点时间
		remark	string		否	备注
		goodsList	list		是	商品list
		其中goodsList包括
		字段名	数据类型	默认值	必填项 	备注
		goodsId	string		是	商品ID
		checkNum	double		是	盘后库存数量
		storageCheckRemark	string		否	商品盘点备注
		storagAfterNumber	double		是	盘点后数量
	*/
	private Long shopId;
	private String token;
	private String  checkNo;
	private Long  operaterId;
	private String  operaterName;
	private String  checkTime;
	private String  remark;
	private List<StorageCheckGoodsDto>  goodsList;
	
	
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
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
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
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<StorageCheckGoodsDto> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<StorageCheckGoodsDto> goodsList) {
		this.goodsList = goodsList;
	}
	
	
}
