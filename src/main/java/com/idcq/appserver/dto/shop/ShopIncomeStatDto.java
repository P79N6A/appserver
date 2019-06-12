package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

/**
 * 商铺账务统计表实体
 * @author ChenYongxin
 *
 */
public class ShopIncomeStatDto implements Serializable{
	private static final long serialVersionUID = -709688571311980762L;
	/**
	 * 结算日期
	 */
	private Date statDate;
	/**
	 * 商铺id
	 */
	private Long shopId;
	/**
	 * 订单id
	 */
	private String orderId;
	/**
	 * 订单标题
	 */
	private String orderTitle;
	/**
	 * 手机
	 */
	private String mobile;
	/**
	 * 结算价格
	 */
	private Double settlePrice;
	/**
	 * 订单时间
	 */
	private Date orderTime;
	/**
	 * 现金支付
	 */
	private Double cashPay;
	/**
	 * pos支付
	 */
	private Double posPay;
	/**
	 * 线上支付
	 */
	private Double onLinePay;
	
	/**
	 * 会员卡支付
	 */
	private Double memberCardPay;
	
	/**
	 * 收银员编号，老板账号=0
	 */
	private Long cashierId;
	/**
	 * 商铺服务费
	 */
	private Double platformServeSharePrice;
	
	private Integer incomeType;
	
	private Date finishTime;
	
	private Double chargePrice;
	
	/**
	 * 代金券支付
	 */
	private Double voucherPay;
	
	private Double freePay;
	
	private Double customPay1;
	
	private Double customPay2;
	
	private Double customPay3;
	
	public Double getPlatformServeSharePrice()
    {
        return platformServeSharePrice;
    }
    public void setPlatformServeSharePrice(Double platformServeSharePrice)
    {
        this.platformServeSharePrice = platformServeSharePrice;
    }
    public Date getStatDate() {
		return statDate;
	}
	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderTitle() {
		return orderTitle;
	}
	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Double getSettlePrice() {
		return settlePrice;
	}
	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Double getCashPay() {
		return cashPay;
	}
	public void setCashPay(Double cashPay) {
		this.cashPay = cashPay;
	}
	public Double getPosPay() {
		return posPay;
	}
	public void setPosPay(Double posPay) {
		this.posPay = posPay;
	}
	public Double getOnLinePay() {
		return onLinePay;
	}
	public void setOnLinePay(Double onLinePay) {
		this.onLinePay = onLinePay;
	}
	public Long getCashierId() {
		return cashierId;
	}
	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}
	public Double getMemberCardPay() {
		return memberCardPay;
	}
	public void setMemberCardPay(Double memberCardPay) {
		this.memberCardPay = memberCardPay;
	}
    public Integer getIncomeType() {
        return incomeType;
    }
    public void setIncomeType(Integer incomeType) {
        this.incomeType = incomeType;
    }
    public Date getFinishTime() {
        return finishTime;
    }
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
    public Double getChargePrice() {
        return chargePrice;
    }
    public void setChargePrice(Double chargePrice) {
        this.chargePrice = chargePrice;
    }
    public Double getVoucherPay() {
        return voucherPay;
    }
    public void setVoucherPay(Double voucherPay) {
        this.voucherPay = voucherPay;
    }
    public Double getFreePay() {
        return freePay;
    }
    public void setFreePay(Double freePay) {
        this.freePay = freePay;
    }
    public Double getCustomPay1() {
        return customPay1;
    }
    public void setCustomPay1(Double customPay1) {
        this.customPay1 = customPay1;
    }
    public Double getCustomPay2() {
        return customPay2;
    }
    public void setCustomPay2(Double customPay2) {
        this.customPay2 = customPay2;
    }
    public Double getCustomPay3() {
        return customPay3;
    }
    public void setCustomPay3(Double customPay3) {
        this.customPay3 = customPay3;
    }
	
	
}