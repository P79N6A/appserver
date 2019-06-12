package com.idcq.appserver.dto.shopcart;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 
 * @author Administrator
 * 
 * @date 2015年3月13日
 * @time 上午10:58:39
 */
public class ShopCartDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7823384938717417211L;
	private Integer shoppingCartId;
    private Long userId;			//会员ID
    private Long goodsId;		//商品ID
    private Integer goodsNumber;	//数量
    private Integer goodsIndex;		//排序
    
    /*---------------------------*/
    private List<ShopCartItemDto> goodsList;	//购物车商品列表

	public ShopCartDto() {
		super();
	}

	public Integer getShoppingCartId() {
		return shoppingCartId;
	}

	public void setShoppingCartId(Integer shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public Integer getGoodsIndex() {
		return goodsIndex;
	}

	public void setGoodsIndex(Integer goodsIndex) {
		this.goodsIndex = goodsIndex;
	}


	public List<ShopCartItemDto> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<ShopCartItemDto> goodsList) {
		this.goodsList = goodsList;
	}

	@Override
	public String toString() {
		return "ShopCartDto [shoppingCartId=" + shoppingCartId + ", userId="
				+ userId + ", goodsId=" + goodsId + ", goodsNumber="
				+ goodsNumber + ", goodsIndex=" + goodsIndex + ", goodsList="
				+ goodsList + "]";
	}
    
    

}