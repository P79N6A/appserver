package com.idcq.appserver.utils.settle;
/**
 * 订单结算各项金额封装
 * @ClassName: OrderSettleAmountDto 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月22日 下午2:24:51 
 *
 */
public class OrderSettleUserAmountDto {
	
	/**
	 * 店铺编号
	 */
	private Long shopId;
	
	/**
	 * 订单结算标识
	 */
	private Integer userShareSettleFlag;
	
	/**
	 * 用户推荐人 
	 */
	private Long referUserId;
	
	/**
	 * 店铺服务人员
	 */
	private Long shopServerUserId;
	
	/**
	 * 店铺服务人员分成金额
	 */
	private Double shopServerSharePrice;
	
	/**
	 * 一级代理用户
	 */
	private Long level1AgentShareUserId;
	
	/**
	 * 一级代理分成金额
	 */
	private Double level1AgentPrice;
	
	
	/**
	 * 二级代理用户
	 */
	private Long level2AgentShareUserId;
	
	/**
	 * 二级代理分成金额
	 */
	private Double level2AgentPrice;
	
	/**
	 * 三级代理用户
	 */
	private Long level3AgentShareUserId;
	
	/**
	 * 三级代理分成金额
	 */
	private Double level3AgentPrice;
	
	/**
	 * 用户消费返点金额
	 */
	private Double userSharePrice;
	
	/**
	 * 会员推荐分成金额
	 */
	private Double userReferSharePrice;
	
	
	/**
	 * 店铺推荐人
	 */
	private Long shopReferShareUserId;
	
	/**
	 * 店铺推荐人分成金额
	 */
	private Double shopReferSharePrice;
	
	/**
	 * 消费的用户
	 */
	private Long userId;
	
	

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getUserShareSettleFlag() {
		return userShareSettleFlag;
	}

	public void setUserShareSettleFlag(Integer userShareSettleFlag) {
		this.userShareSettleFlag = userShareSettleFlag;
	}

	public Long getReferUserId() {
		return referUserId;
	}

	public void setReferUserId(Long referUserId) {
		this.referUserId = referUserId;
	}

	public Long getShopServerUserId() {
		return shopServerUserId;
	}

	public void setShopServerUserId(Long shopServerUserId) {
		this.shopServerUserId = shopServerUserId;
	}

	public Long getLevel1AgentShareUserId() {
		return level1AgentShareUserId;
	}

	public void setLevel1AgentShareUserId(Long level1AgentShareUserId) {
		this.level1AgentShareUserId = level1AgentShareUserId;
	}

	public Double getLevel1AgentPrice() {
		return level1AgentPrice;
	}

	public void setLevel1AgentPrice(Double level1AgentPrice) {
		this.level1AgentPrice = level1AgentPrice;
	}

	public Long getLevel2AgentShareUserId() {
		return level2AgentShareUserId;
	}

	public void setLevel2AgentShareUserId(Long level2AgentShareUserId) {
		this.level2AgentShareUserId = level2AgentShareUserId;
	}

	public Double getLevel2AgentPrice() {
		return level2AgentPrice;
	}

	public void setLevel2AgentPrice(Double level2AgentPrice) {
		this.level2AgentPrice = level2AgentPrice;
	}

	public Long getLevel3AgentShareUserId() {
		return level3AgentShareUserId;
	}

	public void setLevel3AgentShareUserId(Long level3AgentShareUserId) {
		this.level3AgentShareUserId = level3AgentShareUserId;
	}

	public Double getLevel3AgentPrice() {
		return level3AgentPrice;
	}

	public void setLevel3AgentPrice(Double level3AgentPrice) {
		this.level3AgentPrice = level3AgentPrice;
	}

	public Double getUserSharePrice() {
		return userSharePrice;
	}

	public void setUserSharePrice(Double userSharePrice) {
		this.userSharePrice = userSharePrice;
	}

	public Double getUserReferSharePrice() {
		return userReferSharePrice;
	}

	public void setUserReferSharePrice(Double userReferSharePrice) {
		this.userReferSharePrice = userReferSharePrice;
	}

	public Double getShopReferSharePrice() {
		return shopReferSharePrice;
	}

	public void setShopReferSharePrice(Double shopReferSharePrice) {
		this.shopReferSharePrice = shopReferSharePrice;
	}

	public Double getShopServerSharePrice() {
		return shopServerSharePrice;
	}

	public void setShopServerSharePrice(Double shopServerSharePrice) {
		this.shopServerSharePrice = shopServerSharePrice;
	}

	public Long getShopReferShareUserId() {
		return shopReferShareUserId;
	}

	public void setShopReferShareUserId(Long shopReferShareUserId) {
		this.shopReferShareUserId = shopReferShareUserId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
