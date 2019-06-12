package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.util.List;

/** 
* @ClassName: TimeCardDto 
* @Description: 次卡封装实体类
* @author dengjihai
* @date 2016年2月16日 下午4:32:17  
*/ 
@SuppressWarnings("serial")
public class TimeCardDto extends GoodsDto implements Serializable {

	// -- 套餐(次卡)新增字段（为接口提供需要）---//
    private String goodsSetName;//套餐名称
    private Integer goodsSetType;//套餐类型 3000:套餐  4000:次卡
    private String goodsSetId;//套餐id
	private String token;
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private List<GoodsSetDto> goodsList;//套餐内的商品列表

	public String getGoodsSetName() {
		return goodsSetName;
	}

	public void setGoodsSetName(String goodsSetName) {
		this.goodsSetName = goodsSetName;
	}

	public Integer getGoodsSetType() {
		return goodsSetType;
	}

	public void setGoodsSetType(Integer goodsSetType) {
		this.goodsSetType = goodsSetType;
	}

	public String getGoodsSetId() {
		return goodsSetId;
	}

	public void setGoodsSetId(String goodsSetId) {
		this.goodsSetId = goodsSetId;
	}

	public List<GoodsSetDto> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsSetDto> goodsList) {
		this.goodsList = goodsList;
	}
    
    
}
