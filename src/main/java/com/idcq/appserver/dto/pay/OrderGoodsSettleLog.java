package com.idcq.appserver.dto.pay;

import java.util.Date;

public class OrderGoodsSettleLog {
    /**
     *主键
     */
    private Long logId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     *用户ID
     */
    private Long userId;

    /**
     *商铺id
     */
    private Long shopId;

    /**
     *商品id
     */
    private Long goodsId;

    /**
     * 日志最后创建时间
     */
    private Date createTime;

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
