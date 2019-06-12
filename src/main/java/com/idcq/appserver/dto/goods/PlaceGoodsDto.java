package com.idcq.appserver.dto.goods;

import java.io.Serializable;

/**
 * 场地类预定时间列表
 * @author Administrator
 *
 */
public class PlaceGoodsDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String groupGoodsId;
	private String goodsId;
	private String fromTime;
	private String toTime;
	private String price;
	private int status; // 资源状态,枚举值：1-可用，2-不可用
	
	
	
	public String getGroupGoodsId() {
		return groupGoodsId;
	}
	public void setGroupGoodsId(String groupGoodsId) {
		this.groupGoodsId = groupGoodsId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
