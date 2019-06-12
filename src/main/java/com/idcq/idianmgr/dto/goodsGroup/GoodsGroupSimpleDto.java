package com.idcq.idianmgr.dto.goodsGroup;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品族dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 上午10:28:26
 */
public class GoodsGroupSimpleDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5389781876718303051L;
	private Long goodsGroupId;
    private Integer goodsServerMode;
    private String goodsName;
    private String goodsLogo;
    private Integer goodsStatus;
    private Long soldNumber;
    /*------------20150731--------------*/
    private String goodsPrice;
    /*------------20150817--------------*/
    private Long goodsLogoId;
    
	public GoodsGroupSimpleDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getGoodsGroupId() {
		return goodsGroupId;
	}
	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}
	public Integer getGoodsServerMode() {
		return goodsServerMode;
	}
	public void setGoodsServerMode(Integer goodsServerMode) {
		this.goodsServerMode = goodsServerMode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsLogo() {
		return goodsLogo;
	}
	public void setGoodsLogo(String goodsLogo) {
		this.goodsLogo = goodsLogo;
	}
	public Integer getGoodsStatus() {
		return goodsStatus;
	}
	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}
	
	public Long getSoldNumber() {
		return soldNumber;
	}
	public void setSoldNumber(Long soldNumber) {
		this.soldNumber = soldNumber;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public Long getGoodsLogoId() {
		return goodsLogoId;
	}
	public void setGoodsLogoId(Long goodsLogoId) {
		this.goodsLogoId = goodsLogoId;
	}
	
    

}