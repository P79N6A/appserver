package com.idcq.appserver.dto.user;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 用户(会员)dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月11日
 * @time 上午10:49:20
 */
public class UserAccountDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 4447550589523390537L;

    @JsonIgnore
    private Long accountId;
    @JsonIgnore
    private Long userId; 

    private Double amount;

    @JsonIgnore
    private Integer accountStatus; // 账户状态，正常-1，冻结-0

    @JsonIgnore
    private String contact; // 联系人

    @JsonIgnore
    private String telephone; // 电话

    @JsonIgnore
    private Date createTime;// 创建时间

    @JsonIgnore
    private String userRole;

    /**
     * 冻结金额
     */
    @JsonIgnore
    private Double freezeAmount;
    /**
     * 消费金
     */
    private Double couponAmount;
    /**
     * 奖励金额
     */
    private Double rewardAmount;
    /**
     * 奖励总额
     */
    private Double rewardTotal;

    /**
     * 累计传奇币总额
     */
    @JsonIgnore
    private Double legendTotal;


    /**
     * 消费币余额
     */
    @JsonIgnore
    private Double consumeAmount;

    /**
     * 累计消费币总额
     */
    @JsonIgnore
    private Double consumeTotal;

    /**
     * 代金券余额
     */
    @JsonIgnore
    private Double voucherAmount;

    /**
     * 累计代金券总额
     */
    @JsonIgnore
    private Double voucherTotal;

    /**
     * 扣减收益循环计数值,每达到上限值需减去上限值,普通C不计入
     */
    @JsonIgnore
    private Double deductionCountValue;

    /**
     * 累计扣减总额
     */
    @JsonIgnore
    private Double deductionTotal;

    /**
     * 消费返还总额
     */
    @JsonIgnore
    private Double consumeRebateTotal;

    /**
     * 已经消费返还总额
     */

    @JsonIgnore
    private Double consumeRebateMoney;

    @JsonIgnore
    private Double salesTotal;//累计节省金额
    
    private Double couponRebatesTotal;//累计消费金奖励

    public Double getConsumeRebateMoney()
    {
        return consumeRebateMoney;
    }

    public void setConsumeRebateMoney(Double consumeRebateMoney)
    {
        this.consumeRebateMoney = consumeRebateMoney;
    }

    public Double getLegendTotal()
    {
        return legendTotal;
    }

    public void setLegendTotal(Double legendTotal)
    {
        this.legendTotal = legendTotal;
    }

    public Double getConsumeAmount()
    {
        return consumeAmount;
    }

    public void setConsumeAmount(Double consumeAmount)
    {
        this.consumeAmount = consumeAmount;
    }

    public Double getConsumeTotal()
    {
        return consumeTotal;
    }

    public void setConsumeTotal(Double consumeTotal)
    {
        this.consumeTotal = consumeTotal;
    }

    public Double getVoucherAmount()
    {
        return voucherAmount;
    }

    public void setVoucherAmount(Double voucherAmount)
    {
        this.voucherAmount = voucherAmount;
    }

    public Double getVoucherTotal()
    {
        return voucherTotal;
    }

    public void setVoucherTotal(Double voucherTotal)
    {
        this.voucherTotal = voucherTotal;
    }

    public Double getDeductionCountValue()
    {
        return deductionCountValue;
    }

    public void setDeductionCountValue(Double deductionCountValue)
    {
        this.deductionCountValue = deductionCountValue;
    }

    public Double getDeductionTotal()
    {
        return deductionTotal;
    }

    public void setDeductionTotal(Double deductionTotal)
    {
        this.deductionTotal = deductionTotal;
    }

    public Double getConsumeRebateTotal()
    {
        return consumeRebateTotal;
    }

    public void setConsumeRebateTotal(Double consumeRebateTotal)
    {
        this.consumeRebateTotal = consumeRebateTotal;
    }


    public Double getFreezeAmount()
    {
        return freezeAmount;
    }

    public void setFreezeAmount(Double freezeAmount)
    {
        this.freezeAmount = freezeAmount;
    }

    public UserAccountDto()
    {
        super();
    }

    public Long getAccountId()
    {
        return accountId;
    }

    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
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

    public String getUserRole()
    {
        return userRole;
    }

    public void setUserRole(String userRole)
    {
        this.userRole = userRole;
    }

    public Double getCouponAmount()
    {
        return couponAmount;
    }

    public void setCouponAmount(Double couponAmount)
    {
        this.couponAmount = couponAmount;
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

	public Double getSalesTotal() {
		return salesTotal;
	}

	public void setSalesTotal(Double salesTotal) {
		this.salesTotal = salesTotal;
	}

    public Double getCouponRebatesTotal() {
        return couponRebatesTotal;
    }

    public void setCouponRebatesTotal(Double couponRebatesTotal) {
        this.couponRebatesTotal = couponRebatesTotal;
    }
    

}