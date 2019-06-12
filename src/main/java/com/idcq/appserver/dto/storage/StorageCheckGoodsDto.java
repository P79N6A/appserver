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

public class StorageCheckGoodsDto implements Serializable {

  /*	
		字段名	数据类型	默认值	必填项 	备注
		goodsId	string		是	商品ID
		checkNum	double		是	盘后库存数量
		storageCheckRemark	string		否	商品盘点备注
		storagAfterNumber	double		是	盘点后数量
	
	*/
	
	private static final long serialVersionUID = -346216675417619147L;
	
	private Long goodsId;
	private Double checkNum;
	private Double storagAfterNumber;
	private String storageCheckRemark;
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Double getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(Double checkNum) {
		this.checkNum = checkNum;
	}
	public Double getStoragAfterNumber() {
		return storagAfterNumber;
	}
	public void setStoragAfterNumber(Double storagAfterNumber) {
		this.storagAfterNumber = storagAfterNumber;
	}
	public String getStorageCheckRemark() {
		return storageCheckRemark;
	}
	public void setStorageCheckRemark(String storageCheckRemark) {
		this.storageCheckRemark = storageCheckRemark;
	}

	
}	
