/**
 * 
 */
package com.idcq.appserver.dto.goods;

import java.math.BigDecimal;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: GoodsSetDto
 * @Description: TODO(套餐)
 * @author 张鹏程
 * @date 2015年4月18日 上午11:09:19
 * 
 */
public class GoodsSetDto {

	/**
	 * 商品Id
	 */
	private Long goodsId;
	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 商品类型
	 */
	private Integer goodsType;

	/**
	 * 商品数量
	 */
	private Double goodsNumber;

	private Integer goodsIndex=1;

	private String goodsGroup;

	private String goodsNo;

	private String goodsLogo1;

	private String goodsLogo2;

	private String goodsUrl;

	private Long soldNumber;

	private Long zanNumber;

	private Double standardPrice;

	private Double discountPrice;

	/**
	 * 会员价
	 */
	private Double vipPrice;

	/**
	 * 折上折价格
	 */
	private Double finalPrice;

	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 商品分类编号
	 */
	private Long goodsCategoryId;

	/**
	 * 商品种类名称
	 */
	private String goodsCategoryName;

	/**
	 * 商品简述
	 */
	private String goodsDesc;

	/**
	 * 商品详述
	 */
	private String goodsDetailDesc;

	/**
	 * 商品口味
	 */
	private String taste;

	/**
	 * 辣度
	 */
	private String spiciness;

	/**
	 * 时候需要专家
	 */
	private Integer isExpert;

	/**
	 * 专家列表
	 */
	private String expertIds;

	/**
	 * 是否参与打折
	 */
	private Integer goodsSettleFlag;
	/*--套餐（次卡）-新增字段*/
	private Long goodsSetId;// 套餐ID
	private String unitName;
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getGoodsProValuesNames() {
		return goodsProValuesNames;
	}

	public void setGoodsProValuesNames(String goodsProValuesNames) {
		this.goodsProValuesNames = goodsProValuesNames;
	}

	private String goodsProValuesNames;//商品属性名称
	@JsonIgnore
	private Integer flag;

	private BigDecimal price;// 商品套餐内单价

	private String goodsSetName;//
	
	private List<GoodsSetDto> goodsList;
	
	private Integer validityTerm;//有效期（月份为单位）
	
	private Integer status;

	private String barcode;

	public String getBarcode()
	{
		return barcode;
	}

	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	public Integer getValidityTerm() {
		return validityTerm;
	}

	public void setValidityTerm(Integer validityTerm) {
		this.validityTerm = validityTerm;
	}

	public List<GoodsSetDto> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsSetDto> goodsList) {
		this.goodsList = goodsList;
	}

	public String getGoodsSetName() {
		return goodsSetName;
	}

	public void setGoodsSetName(String goodsSetName) {
		this.goodsSetName = goodsSetName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getGoodsSetId() {
		return goodsSetId;
	}

	public void setGoodsSetId(Long goodsSetId) {
		this.goodsSetId = goodsSetId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}

	public Double getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Double goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public Integer getGoodsIndex() {
		return goodsIndex;
	}

	public void setGoodsIndex(Integer goodsIndex) {
		this.goodsIndex = goodsIndex;
	}

	public String getGoodsGroup() {
		return goodsGroup;
	}

	public void setGoodsGroup(String goodsGroup) {
		this.goodsGroup = goodsGroup;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsLogo1() {
		return goodsLogo1;
	}

	public void setGoodsLogo1(String goodsLogo1) {
		this.goodsLogo1 = goodsLogo1;
	}

	public String getGoodsLogo2() {
		return goodsLogo2;
	}

	public void setGoodsLogo2(String goodsLogo2) {
		this.goodsLogo2 = goodsLogo2;
	}

	public String getGoodsUrl() {
		return goodsUrl;
	}

	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}

	public Long getSoldNumber() {
		return soldNumber;
	}

	public void setSoldNumber(Long soldNumber) {
		this.soldNumber = soldNumber;
	}

	public Long getZanNumber() {
		return zanNumber;
	}

	public void setZanNumber(Long zanNumber) {
		this.zanNumber = zanNumber;
	}

	public Double getStandardPrice() {
		return standardPrice;
	}

	public void setStandardPrice(Double standardPrice) {
		this.standardPrice = standardPrice;
	}

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Double getVipPrice() {
		return vipPrice;
	}

	public void setVipPrice(Double vipPrice) {
		this.vipPrice = vipPrice;
	}

	public Double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getGoodsCategoryId() {
		return goodsCategoryId;
	}

	public void setGoodsCategoryId(Long goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}

	public String getGoodsCategoryName() {
		return goodsCategoryName;
	}

	public void setGoodsCategoryName(String goodsCategoryName) {
		this.goodsCategoryName = goodsCategoryName;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getGoodsDetailDesc() {
		return goodsDetailDesc;
	}

	public void setGoodsDetailDesc(String goodsDetailDesc) {
		this.goodsDetailDesc = goodsDetailDesc;
	}

	public String getTaste() {
		return taste;
	}

	public void setTaste(String taste) {
		this.taste = taste;
	}

	public String getSpiciness() {
		return spiciness;
	}

	public void setSpiciness(String spiciness) {
		this.spiciness = spiciness;
	}

	public Integer getIsExpert() {
		return isExpert;
	}

	public void setIsExpert(Integer isExpert) {
		this.isExpert = isExpert;
	}

	public String getExpertIds() {
		return expertIds;
	}

	public void setExpertIds(String expertIds) {
		this.expertIds = expertIds;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getGoodsSettleFlag() {
		return goodsSettleFlag;
	}

	public void setGoodsSettleFlag(Integer goodsSettleFlag) {
		this.goodsSettleFlag = goodsSettleFlag;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
