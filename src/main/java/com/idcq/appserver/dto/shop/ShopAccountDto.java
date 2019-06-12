package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 商铺账户dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月11日
 * @time 上午10:49:20
 */
public class ShopAccountDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 4447550589523390537L;

    @JsonIgnore
    private Integer shopAccountId;

    /**
     * 商铺ID
     */
    @JsonIgnore
    private Long shopId;

    /**
     * 账户总金额
     */
    private Double amount;

    /**
     * 账户状态，正常-1，冻结-0
     */
    @JsonIgnore
    private Integer accountStatus;

    /**
     * 联系人
     */
    @JsonIgnore
    private String contact;
    private Double marketAmount;
    public Double getMarketAmount() {
		return marketAmount;
	}

	public void setMarketAmount(Double marketAmount) {
		this.marketAmount = marketAmount;
	}

	public Double getMarketTotal() {
		return marketTotal;
	}

	public void setMarketTotal(Double marketTotal) {
		this.marketTotal = marketTotal;
	}

	private Double marketTotal;
	
	private Double  marketRebateTotal;
	public Double getMarketRebateTotal() {
		return marketRebateTotal;
	}

	public void setMarketRebateTotal(Double marketRebateTotal) {
		this.marketRebateTotal = marketRebateTotal;
	}

	public Double getMarketRebateMoney() {
		return marketRebateMoney;
	}

	public void setMarketRebateMoney(Double marketRebateMoney) {
		this.marketRebateMoney = marketRebateMoney;
	}

	private Double  marketRebateMoney;
    /**
     * 电话
     */
    @JsonIgnore
    private String telephone;

    /**
     * 创建时间
     */
    @JsonIgnore
    private Date createTime;

    /**
     * 冻结资金
     */
    private Double freezeAmount;

    /**
     * 在线营业收入余额
     */
    private Double onlineIncomeAmount;

    /**
     * 平台奖励余额
     */
    private Double rewardAmount;

    /**
     * 累计平台奖励总额
     */
    private Double rewardTotal;

    /**
     * 保证金余额
     */
    private Double depositAmount;
    /**
     * 手续费
     */
    private Double withdrawCommission;


    /**
     * 累计传奇币总额
     */
    private Double legendTotal;

    public Double getLegendTotal()
    {
        return legendTotal;
    }

    public void setLegendTotal(Double legendTotal)
    {
        this.legendTotal = legendTotal;
    }

    public ShopAccountDto()
    {
        super();
    }

    public Double getFreezeAmount()
    {
        return freezeAmount;
    }

    public void setFreezeAmount(Double freezeAmount)
    {
        this.freezeAmount = freezeAmount;
    }

    public Double getAmount()
    {
        return amount;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    public Integer getAccountStatus()
    {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus)
    {
        this.accountStatus = accountStatus;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Integer getShopAccountId()
    {
        return shopAccountId;
    }

    public void setShopAccountId(Integer shopAccountId)
    {
        this.shopAccountId = shopAccountId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Double getOnlineIncomeAmount()
    {
        return onlineIncomeAmount;
    }

    public void setOnlineIncomeAmount(Double onlineIncomeAmount)
    {
        this.onlineIncomeAmount = onlineIncomeAmount;
    }

    public Double getRewardAmount()
    {
        return rewardAmount;
    }

    public void setRewardAmount(Double rewardAmount)
    {
        this.rewardAmount = rewardAmount;
    }

    public Double getRewardTotal()
    {
        return rewardTotal;
    }

    public void setRewardTotal(Double rewardTotal)
    {
        this.rewardTotal = rewardTotal;
    }

    public Double getDepositAmount()
    {
        return depositAmount;
    }

    public void setDepositAmount(Double depositAmount)
    {
        this.depositAmount = depositAmount;
    }

	public Double getWithdrawCommission() {
		return withdrawCommission;
	}

	public void setWithdrawCommission(Double withdrawCommission) {
		this.withdrawCommission = withdrawCommission;
	}

}