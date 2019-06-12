package com.idcq.idianmgr.dto.goodsGroup;

import java.io.Serializable;

/**
 * 商品族操作dto(新增及修改时使用)
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 下午4:30:08
 */
public class GoodsGroupHandleDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7577515081765713779L;
	private Long shopId;
	private Long goodsGroupId;
	private Integer operateType;
	private String goodsName;
	private Double servicePrice;
	private Integer goodsServerMode;
	private Double workTime;
	private Integer keepDay;
	private String goodsDesc;
	private String attachementIds;
	private String goodsCategoryIds;
	private String techIds;
	
	/*-----------20150817追加-------------*/
	private Long goodsLogoId;
	
	public GoodsGroupHandleDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getGoodsGroupId() {
		return goodsGroupId;
	}
	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Double getServicePrice() {
		return servicePrice;
	}
	public void setServicePrice(Double servicePrice) {
		this.servicePrice = servicePrice;
	}
	public Integer getGoodsServerMode() {
		return goodsServerMode;
	}
	public void setGoodsServerMode(Integer goodsServerMode) {
		this.goodsServerMode = goodsServerMode;
	}
	
	public Double getWorkTime() {
		return workTime;
	}
	public void setWorkTime(Double workTime) {
		this.workTime = workTime;
	}
	public Integer getKeepDay() {
		return keepDay;
	}
	public void setKeepDay(Integer keepDay) {
		this.keepDay = keepDay;
	}
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public String getAttachementIds() {
		return attachementIds;
	}
	public void setAttachementIds(String attachementIds) {
		this.attachementIds = attachementIds;
	}
	public String getGoodsCategoryIds() {
		return goodsCategoryIds;
	}
	public void setGoodsCategoryIds(String goodsCategoryIds) {
		this.goodsCategoryIds = goodsCategoryIds;
	}
	public String getTechIds() {
		return techIds;
	}
	public void setTechIds(String techIds) {
		this.techIds = techIds;
	}
	public Long getGoodsLogoId() {
		return goodsLogoId;
	}
	public void setGoodsLogoId(Long goodsLogoId) {
		this.goodsLogoId = goodsLogoId;
	}
	
	
}
