package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.goods.GoodsAvsDto;
import com.idcq.appserver.dto.goods.GoodsSetDto;

/**
 * 组合支付订单商品信息
 * @author nie_jq
 * @date 2015-11-23
 *
 */
public class MultiPayOrderInfoDto  implements Serializable{
	private static final long serialVersionUID = -5979898710541023661L;
	private Long dishId;
	private Double num;
	private Double standardPrice;//目录价
	private Double shopPrice;//改价后价格（或者单品打折后价格）
	private Double platformPrice;//会员折后价
	private String dishRemark;
	private Long billerId;
	private String specsDesc;
	private Double orderGoodsDiscount;
	private List<GoodsAvsDto> avsSpecsList;//订单商品增值服务信息
	private String setGoodsGroup;
	private Integer isConsumeOuter;	//是否外带标志，0-否，1-是

	public Integer getIsConsumeOuter()
	{
		return isConsumeOuter;
	}

	public void setIsConsumeOuter(Integer isConsumeOuter)
	{
		this.isConsumeOuter = isConsumeOuter;
	}

	public String getSetGoodsGroup() {
		return setGoodsGroup;
	}
	public void setSetGoodsGroup(String setGoodsGroup) {
		this.setGoodsGroup = setGoodsGroup;
	}
	public List<GoodsSetDto> getSetGoodsList() {
		return setGoodsList;
	}
	public void setSetGoodsList(List<GoodsSetDto> setGoodsList) {
		this.setGoodsList = setGoodsList;
	}
	private List<GoodsSetDto> setGoodsList;//套餐内商品
    public List<GoodsAvsDto> getAvsSpecsList() {
		return avsSpecsList;
	}
	public void setAvsSpecsList(List<GoodsAvsDto> avsSpecsList) {
		this.avsSpecsList = avsSpecsList;
	}
	/**
     * 是否取消：0-正常退菜，1-反结账退菜，2-正常下单，3-反结账加菜
     */
    private Integer isCancle = 2;

	 /*
     * 订单的技师列表[新增订单商品关联技师列表]
     * */
    private List<Map<String, Object>> techs;
    
	public List<Map<String, Object>> getTechs() {
		return techs;
	}
	public void setTechs(List<Map<String, Object>> techs) {
		this.techs = techs;
	}
	public Long getDishId() {
		return dishId;
	}
	public void setDishId(Long dishId) {
		this.dishId = dishId;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public String getDishRemark() {
		return dishRemark;
	}
	public void setDishRemark(String dishRemark) {
		this.dishRemark = dishRemark;
	}
	public Long getBillerId() {
		return billerId;
	}
	public void setBillerId(Long billerId) {
		this.billerId = billerId;
	}
	public String getSpecsDesc() {
		return specsDesc;
	}
	public void setSpecsDesc(String specsDesc) {
		this.specsDesc = specsDesc;
	}
	public Double getOrderGoodsDiscount() {
		return orderGoodsDiscount;
	}
	public void setOrderGoodsDiscount(Double orderGoodsDiscount) {
		this.orderGoodsDiscount = orderGoodsDiscount;
	}
	public Double getStandardPrice() {
		return standardPrice;
	}
	public void setStandardPrice(Double standardPrice) {
		this.standardPrice = standardPrice;
	}
	public Double getShopPrice() {
		return shopPrice;
	}
	public void setShopPrice(Double shopPrice) {
		this.shopPrice = shopPrice;
	}
	public Double getPlatformPrice() {
		return platformPrice;
	}
	public void setPlatformPrice(Double platformPrice) {
		this.platformPrice = platformPrice;
	}
	public Integer getIsCancle() {
        return isCancle;
    }
    public void setIsCancle(Integer isCancle) {
        this.isCancle = isCancle;
    }
    @Override
	public String toString() {
		return "MultiPayOrderInfoDto [dishId=" + dishId + ", num=" + num
				+ ", standardPrice=" + standardPrice + ", shopPrice="
				+ shopPrice + ", platformPrice=" + platformPrice
				+ ", dishRemark=" + dishRemark + ", billerId=" + billerId
				+ ", specsDesc=" + specsDesc + ", orderGoodsDiscount="
				+ orderGoodsDiscount + "]";
	}
	
}
