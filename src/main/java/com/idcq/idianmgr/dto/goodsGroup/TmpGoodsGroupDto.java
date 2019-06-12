package com.idcq.idianmgr.dto.goodsGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 更新商品族内商品价格接口
 * json数据接收帮助类
 * @author nie_jq
 *
 */
public class TmpGoodsGroupDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5923934654097175079L;
	private String shopId;
	private String goodsGroupId;
	private List<TmpGoodsDto> goods = new ArrayList<TmpGoodsDto>();
	
	public TmpGoodsGroupDto() {
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	
	public String getGoodsGroupId() {
		return goodsGroupId;
	}
	public void setGoodsGroupId(String goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}
	public List<TmpGoodsDto> getGoods() {
		return goods;
	}
	public void setGoods(List<TmpGoodsDto> goods) {
		this.goods = goods;
	}
	
}
