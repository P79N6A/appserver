package com.idcq.appserver.dto.cashcard;

import java.io.Serializable;
import java.util.Date;

/**
 * 充值卡使用记录实体
 * @author ChenYongxin
 * 
 */
public class CashCardUseLogDto implements Serializable
{

    private static final long serialVersionUID = 252776405664755937L;

    /**
     * 主键
     */
    private Long logId;

    /**
     * 充值卡编码
     */
    private String cashCardNo;

    /**
     * 充值卡批次ID
     */
    private Long cashCardBatchId;

    /**
     * 账号类型：1=用户,2=商铺
     */
    private Integer accountType;

    /**
     * 'account_type=1时user_id,account_type=2时shop_id',
     */
    private Long accountId;

    /**
     * 账号名
     */
    private String accountName;
    /**
     * /手机号码
     */
    private String accountMobile;

    /**
     * 充值卡使用时间
     */
    private Date useTime;

    /**
     * 充值来源系统：1=收银机,2=管家APP,3=消费者APP,4=微信商城,5=公众号,6=商铺后台'
     */
    private Integer fromSystem;

    /**
     * '操作人id,来源为收银机时shop_id，其他均为user_id'
     */
    private Long opertaterId;
    /**
     * '操作人id,来源为收银机时shop_id，其他均为user_id'
     */
    private String opertaterName;
    /**
     * 备注
     */
    private String logDesc;
    
    /***************以下为冗余字段******************/
    /**
     * 充值类型
     */
    private Integer chargeType;

    public Integer getChargeType()
    {
        return chargeType;
    }

    public void setChargeType(Integer chargeType)
    {
        this.chargeType = chargeType;
    }

    public String getLogDesc()
    {
        return logDesc;
    }

    public void setLogDesc(String logDesc)
    {
        this.logDesc = logDesc;
    }

    public Long getLogId()
    {
        return logId;
    }


    public String getOpertaterName()
    {
        return opertaterName;
    }

    public void setOpertaterName(String opertaterName)
    {
        this.opertaterName = opertaterName;
    }

    public String getAccountMobile()
    {
        return accountMobile;
    }

    public void setAccountMobile(String accountMobile)
    {
        this.accountMobile = accountMobile;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
    }

    public String getCashCardNo()
    {
        return cashCardNo;
    }

    public void setCashCardNo(String cashCardNo)
    {
        this.cashCardNo = cashCardNo;
    }

    public Long getCashCardBatchId()
    {
        return cashCardBatchId;
    }

    public void setCashCardBatchId(Long cashCardBatchId)
    {
        this.cashCardBatchId = cashCardBatchId;
    }

    public Integer getAccountType()
    {
        return accountType;
    }

    public void setAccountType(Integer accountType)
    {
        this.accountType = accountType;
    }

    public Long getAccountId()
    {
        return accountId;
    }

    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }

    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public Date getUseTime()
    {
        return useTime;
    }

    public void setUseTime(Date useTime)
    {
        this.useTime = useTime;
    }

    public Integer getFromSystem()
    {
        return fromSystem;
    }

    public void setFromSystem(Integer fromSystem)
    {
        this.fromSystem = fromSystem;
    }

    public Long getOpertaterId()
    {
        return opertaterId;
    }

    public void setOpertaterId(Long opertaterId)
    {
        this.opertaterId = opertaterId;
    }

}
