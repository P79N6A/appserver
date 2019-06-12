package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;

/**
 * 场地分类定价接口
 * json数据接收帮助类
 * @author nie_jq
 *
 */
public class TempGoodsPropertyDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1995137853912850649L;
	private Long goodsId;
	private Integer weekDay;
	private String fromTime;
	private String toTime;
	private Double price;
	private Integer order;
	
	public TempGoodsPropertyDto() {
	}
	
	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(Integer weekDay) {
		this.weekDay = weekDay;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
}
