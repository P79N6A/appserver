package com.idcq.appserver.dto.goods;
/**
 * 外卖设置
* @ClassName: TakeoutSetting 
* @Description: TODO
* @author 张鹏程 
* @date 2015年7月15日 上午8:59:47 
*
 */
public class TakeoutSettingDto {
	
	/**
	 * 设置id
	 */
	private Integer settingId;
	
	/**
	 * 商铺id
	 */
	private Long shopId;
	
	/**
	 * 设置类型
	 */
	private Integer settingType;
	
	/**
	 * 最低起送价格
	 */
	private Float leastBookPrice;

	public Integer getSettingId() {
		return settingId;
	}

	public void setSettingId(Integer settingId) {
		this.settingId = settingId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getSettingType() {
		return settingType;
	}

	public void setSettingType(Integer settingType) {
		this.settingType = settingType;
	}

	public Float getLeastBookPrice() {
		return leastBookPrice;
	}

	public void setLeastBookPrice(Float leastBookPrice) {
		this.leastBookPrice = leastBookPrice;
	}
}
