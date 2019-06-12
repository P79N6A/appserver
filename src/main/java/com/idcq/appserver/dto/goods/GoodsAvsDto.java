package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.common.annotation.Check;

public class GoodsAvsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 219729450125997823L;

	private Long goodsAvsId;
	private String avsName;
	private Long goodsId;
	private Integer avsNumber;
	private Double avsPrice = 0D;
	private Integer avsFlag;
	private Long goodsCategoryId;
	private Integer avsIndex;
	private Date createTime;
	@JsonIgnore
	private Integer pageNo = 1;
	@JsonIgnore
	private Integer pageSize = 10;
	@Check
	private Long shopId; 
	
	
	public Long getGoodsAvsId() {
		return goodsAvsId;
	}
	public void setGoodsAvsId(Long goodsAvsId) {
		this.goodsAvsId = goodsAvsId;
	}
	public String getAvsName() {
		return avsName;
	}
	public void setAvsName(String avsName) {
		this.avsName = avsName;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getAvsNumber() {
		return avsNumber;
	}
	public void setAvsNumber(Integer avsNumber) {
		this.avsNumber = avsNumber;
	}
	public Double getAvsPrice() {
		return avsPrice;
	}
	public void setAvsPrice(Double avsPrice) {
		this.avsPrice = avsPrice;
	}
	public Integer getAvsFlag() {
		return avsFlag;
	}
	public void setAvsFlag(Integer avsFlag) {
		this.avsFlag = avsFlag;
	}
	public Long getGoodsCategoryId() {
		return goodsCategoryId;
	}
	public void setGoodsCategoryId(Long goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getAvsIndex() {
		return avsIndex;
	}
	public void setAvsIndex(Integer avsIndex) {
		this.avsIndex = avsIndex;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
