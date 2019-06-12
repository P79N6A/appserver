package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ShopMemberLevelDto implements Serializable{


    /**
	 * 
	 */
	private static final long serialVersionUID = -2130802982794293687L;
	@JsonIgnore
	private String token;
	/**
	 * 店内会员等级Id
	 */
	private Integer shopMemberLevelId;
	
	/**
	 * 店铺Id
	 */
    private Long shopId;
    
    /**
	 * 店内会员等级名称
	 */
    private String shopMemberLevelName;
    
    /**
	 * 消费最小额度
	 */
    private Double consumeMinAmount;
    
    /**
	 * 消费最大额度
	 */
    private Double consumeMaxAmout;
    
    /**
	 * 店内会员优惠折扣比例
	 */
    private Double discount;
    
    /**
	 * 是否允许自动升级：允许-1；不允许-0
	 */
    private Integer isAutoUpgrate;
    
    /**
	 * 是否删除：是-1，否-0
	 */
    private Integer isDelete;
    
    /**
	 * 等级描述
	 */
    private String levelDescription;
    
    /**
	 * 创建时间
	 */
    private Date createTime;
    
    /**
     * 包含的店内会员数
     */
    private Integer containMemberNum;

	public Integer getShopMemberLevelId() {
		return shopMemberLevelId;
	}

	public void setShopMemberLevelId(Integer shopMemberLevelId) {
		this.shopMemberLevelId = shopMemberLevelId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopMemberLevelName() {
		return shopMemberLevelName;
	}

	public void setShopMemberLevelName(String shopMemberLevelName) {
		this.shopMemberLevelName = shopMemberLevelName;
	}

	public Double getConsumeMinAmount() {
		return consumeMinAmount;
	}

	public void setConsumeMinAmount(Double consumeMinAmount) {
		this.consumeMinAmount = consumeMinAmount;
	}

	public Double getConsumeMaxAmout() {
		return consumeMaxAmout;
	}

	public void setConsumeMaxAmout(Double consumeMaxAmout) {
		this.consumeMaxAmout = consumeMaxAmout;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getIsAutoUpgrate() {
		return isAutoUpgrate;
	}

	public void setIsAutoUpgrate(Integer isAutoUpgrate) {
		this.isAutoUpgrate = isAutoUpgrate;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getLevelDescription() {
		return levelDescription;
	}

	public void setLevelDescription(String levelDescription) {
		this.levelDescription = levelDescription;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getContainMemberNum() {
		return containMemberNum;
	}

	public void setContainMemberNum(Integer containMemberNum) {
		this.containMemberNum = containMemberNum;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
   
    
    

}