package com.idcq.idianmgr.dto.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.MyDateSerializer;
import com.idcq.appserver.utils.MyDateTimeSerializer;


/**
 * 商铺订单详情dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月31日
 * @time 下午1:57:58
 */
public class ShopOrderDetailDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1602358069847540332L;
	private Long userId;
	private String userName;
	private String mobile;
	private Long shopId;
	private String shopName;
	private String orderTitle;
	private Double orderTotalPrice;
	private Integer orderStatus;
	private Long addressId;
	private String userRemark;
	private Double logisticsPrice;
	private Integer payStatus;
	@JsonSerialize(using=MyDateTimeSerializer.class)
	private Date orderTime;
	private Double settlePrice;
	private Double payedAmount;
	private Double notPayedAmount;
	private List<Map> goods;
	
	public ShopOrderDetailDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public String getOrderTitle() {
		return orderTitle;
	}
	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}
	public Double getOrderTotalPrice() {
		return orderTotalPrice;
	}
	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	public Double getLogisticsPrice() {
		return logisticsPrice;
	}
	public void setLogisticsPrice(Double logisticsPrice) {
		this.logisticsPrice = logisticsPrice;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Double getSettlePrice() {
		return settlePrice;
	}
	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
	}
	public Double getPayedAmount() {
		return payedAmount;
	}
	public void setPayedAmount(Double payedAmount) {
		this.payedAmount = payedAmount;
	}
	public Double getNotPayedAmount() {
		return notPayedAmount;
	}
	public void setNotPayedAmount(Double notPayedAmount) {
		this.notPayedAmount = notPayedAmount;
	}
	public List<Map> getGoods() {
		return goods;
	}
	public void setGoods(List<Map> goods) {
		this.goods = goods;
	}
	
	
}