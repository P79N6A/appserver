package com.idcq.appserver.dto.rebates;

import java.io.Serializable;
import java.util.Date;
/**
 * 店铺返利实体
 * 
 * @author ChenYongxin
 *
 */
public class ShopRebatesDto implements Serializable{
	private static final long serialVersionUID = 2808892404452685065L;
	
	
/*	  `shop_id` int(10) unsigned NOT NULL COMMENT '店铺ID',
	  `buy_v_money` decimal(12,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '店铺购买V产品金额',
	  `shop_wait_rebates_money` decimal(12,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '店铺待返还金额',
	  `b_buy_v_rebates_ratio` decimal(6,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '日均返还比例',
	  `is_finish` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '是否完成会员返利：0=未完成，1=完成',
	  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
	  `create_time` datetime DEFAULT NULL COMMENT '创建时间',*/
	private Long shopId;
    private Double buyvMoney;
    private Double shopWaitRebatesMoney;
    private Double bBuyvRebatesRatio;
    private Integer isFinish;
    private Date lastUpdateTime;
    private Date createTime;
    private Integer limit;
    private Integer pageSize;
    private String orderId;
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Double getBuyvMoney() {
		return buyvMoney;
	}
	public void setBuyvMoney(Double buyvMoney) {
		this.buyvMoney = buyvMoney;
	}
	public Double getShopWaitRebatesMoney() {
		return shopWaitRebatesMoney;
	}
	public void setShopWaitRebatesMoney(Double shopWaitRebatesMoney) {
		this.shopWaitRebatesMoney = shopWaitRebatesMoney;
	}
	public Double getbBuyvRebatesRatio() {
		return bBuyvRebatesRatio;
	}
	public void setbBuyvRebatesRatio(Double bBuyvRebatesRatio) {
		this.bBuyvRebatesRatio = bBuyvRebatesRatio;
	}
	public Integer getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(Integer isFinish) {
		this.isFinish = isFinish;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
    
	

}