package com.idcq.appserver.dto.goods;
import java.io.Serializable;


/**
 * 
 * 次卡限定商品表
 * 
 **/
@SuppressWarnings("serial")
public class ShopMemberCardGoods implements Serializable {

	/**次卡关联服务ID**/
	private Long goodsId;

	/**次卡ID**/
	private Long cardId;

	/**服务名称**/
	private String goodsName;

	/**商品套餐内单价**/
	private java.math.BigDecimal price;

	/**商品数量**/
	private Integer goodsNumber;

	/**已使用次数**/
	private Integer usedTimes;



	public void setGoodsId(Long goodsId){
		this.goodsId = goodsId;
	}

	public Long getGoodsId(){
		return this.goodsId;
	}

	public void setCardId(Long cardId){
		this.cardId = cardId;
	}

	public Long getCardId(){
		return this.cardId;
	}

	public void setGoodsName(String goodsName){
		this.goodsName = goodsName;
	}

	public String getGoodsName(){
		return this.goodsName;
	}

	public void setPrice(java.math.BigDecimal price){
		this.price = price;
	}

	public java.math.BigDecimal getPrice(){
		return this.price;
	}

	public void setGoodsNumber(Integer goodsNumber){
		this.goodsNumber = goodsNumber;
	}

	public Integer getGoodsNumber(){
		return this.goodsNumber;
	}

	public void setUsedTimes(Integer usedTimes){
		this.usedTimes = usedTimes;
	}

	public Integer getUsedTimes(){
		return this.usedTimes;
	}

}
