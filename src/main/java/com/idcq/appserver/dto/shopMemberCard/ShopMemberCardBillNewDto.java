package com.idcq.appserver.dto.shopMemberCard;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.MyDateTimeSerializer;

/**
 * 店铺会员卡账单
 * @ClassName: ShopMemberCardBillDto 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年1月14日 下午1:43:20 
 *
 */
public class ShopMemberCardBillNewDto {
	/**
	 * 账单时间
	 */
	private Date billTime;
	/**
	 * 会员卡id
	 */
	private Integer cardId;
	
	/** 
	* @Fields cardType : 1.充值卡，2.次卡
	*/ 
	private String cardType;//卡类型
	
	/**
	 * 账单类型
	 */
	private Integer billType;
	/**
	 * 账单后金额
	 */
	private Double afterAmount;
	/**
	 * 充值类型
	 */
	private Integer payType;
	private String mobile;
	private Double billMoney;
	private Integer billId;
	private String userName;
	private Double presentMoney;
	private String cardBillDesc;
	//收益人
	private String favoree;
	
	//订单标题
	private String orderTitle;
	//账单金额
	private Double billAmount;
	
	private String orderId;
	/**
	 * 账单开始时间
	 */
	@JsonIgnore
	private String billStartTime;
	
	/**
	 * 账单结束时间
	 */
	@JsonIgnore
	private String billEndTime;
	/**
	 * 店铺编号
	 */
	@JsonIgnore
	private Integer shopId;
	
	private Integer opertaterId;
	@JsonIgnore
	private int pageSize;
	@JsonIgnore
	private int pageNo;
	//是否模糊搜索
	@JsonIgnore
	private Integer isSearch;
	
	private Long billerId;
	@JsonSerialize(using=MyDateTimeSerializer.class)
	private Date clientRechargeTime;
	
	public Date getBillTime() {
		return billTime;
	}
	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}
	public Integer getCardId() {
		return cardId;
	}
	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public Double getAfterAmount() {
		return afterAmount;
	}
	public void setAfterAmount(Double afterAmount) {
		this.afterAmount = afterAmount;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Double getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(Double billMoney) {
		this.billMoney = billMoney;
	}
	public Integer getBillId() {
		return billId;
	}
	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getBillStartTime() {
		return billStartTime;
	}
	public void setBillStartTime(String billStartTime) {
		this.billStartTime = billStartTime;
	}
	public String getBillEndTime() {
		return billEndTime;
	}
	public void setBillEndTime(String billEndTime) {
		this.billEndTime = billEndTime;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
    public Double getPresentMoney()
    {
        return presentMoney;
    }
    public void setPresentMoney(Double presentMoney)
    {
        this.presentMoney = presentMoney;
    }
    public String getFavoree()
    {
        return favoree;
    }
    public void setFavoree(String favoree)
    {
        this.favoree = favoree;
    }
    public String getCardBillDesc()
    {
        return cardBillDesc;
    }
    public void setCardBillDesc(String cardBillDesc)
    {
        this.cardBillDesc = cardBillDesc;
    }
    public Integer getOpertaterId()
    {
        return opertaterId;
    }
    public void setOpertaterId(Integer opertaterId)
    {
        this.opertaterId = opertaterId;
    }
    public Integer getIsSearch()
    {
        return isSearch;
    }
    public void setIsSearch(Integer isSearch)
    {
        this.isSearch = isSearch;
    }
    public String getOrderId()
    {
        return orderId;
    }
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
	public String getOrderTitle() {
		return orderTitle;
	}
	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}
	public Double getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}
	public Long getBillerId() {
		return billerId;
	}
	public void setBillerId(Long billerId) {
		this.billerId = billerId;
	}
    public Date getClientRechargeTime() {
        return clientRechargeTime;
    }
    public void setClientRechargeTime(Date clientRechargeTime) {
        this.clientRechargeTime = clientRechargeTime;
    }
	
    
	
	
	
}
