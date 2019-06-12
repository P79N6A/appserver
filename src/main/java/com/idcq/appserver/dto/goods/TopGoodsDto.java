package com.idcq.appserver.dto.goods;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 热卖商品dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月10日
 * @time 下午3:31:48
 */
public class TopGoodsDto implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2370433510182820842L;
	
	@JsonIgnore
	private Long hotGoodsId;
	@JsonIgnore
	private String topType;			//类型
	private Integer goodsId;		//商品ID
	@JsonProperty(value="index")
	private Integer goodsIndex;		
	private Long soldNumber;		//销售次数
	private Long zanNumber;		//点赞次数
	private Integer cityId	;
	/*------------追加商品详细信息-----------*/
	private String goodsName;		//商品名称
	private Integer goodsType;		//商品类型
	private String goodsLogo1;	
	private String goodsLogo2;
	private String goodsUrl;		//链接
	private Long shopId;			//店铺ID
	private String shopName;		
	private Double longitude;		//经度
	private Double latitude; 		//纬度
	private String goodsNo;		
	
	public TopGoodsDto() {
		super();
	}


	public Long getHotGoodsId() {
		return hotGoodsId;
	}


	public void setHotGoodsId(Long hotGoodsId) {
		this.hotGoodsId = hotGoodsId;
	}


	public String getTopType() {
		return topType;
	}


	public void setTopType(String topType) {
		this.topType = topType;
	}


	public Integer getGoodsId() {
		return goodsId;
	}


	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}


	public Integer getGoodsIndex() {
		return goodsIndex;
	}


	public void setGoodsIndex(Integer goodsIndex) {
		this.goodsIndex = goodsIndex;
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

	public Integer getCityId() {
		return cityId;
	}


	public void setCityId(Integer cityId) {
		this.cityId = cityId;
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


	public String getGoodsNo() {
		return goodsNo;
	}


	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}


	public String getGoodsUrl() {
		return goodsUrl;
	}


	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}


	public Long getShopId() {
		return shopId;
	}


	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}


	public String getShopName() {
		return shopName;
	}


	public void setShopName(String shopName) {
		this.shopName = shopName;
	}


	public Double getLongitude() {
		return longitude;
	}


	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	public Double getLatitude() {
		return latitude;
	}


	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	

}