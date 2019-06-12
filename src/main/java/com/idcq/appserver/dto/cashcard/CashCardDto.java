package com.idcq.appserver.dto.cashcard;

import java.io.Serializable;
import java.util.Date;

/**
 * 充值卡实体
 * @author ChenYongxin
 * 
 */
public class CashCardDto implements Serializable
{

    private static final long serialVersionUID = -2764261278560998191L;

    /**
     * 充值卡编码
     */
    private String cashCardNo;

    /**
     * 充值卡密码
     */
    private String cardPassword;

    /**
     * 充值卡批次ID
     */
    private Long cashCardBatchId;

    /**
     * 充值卡状态：失效=0、有效=1、删除=2
     */
    private Integer cardStatus;

    /**
     * 排序
     */
    private Long cardIndex;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getCashCardNo()
    {
        return cashCardNo;
    }

    public void setCashCardNo(String cashCardNo)
    {
        this.cashCardNo = cashCardNo;
    }

    public String getCardPassword()
    {
        return cardPassword;
    }

    public void setCardPassword(String cardPassword)
    {
        this.cardPassword = cardPassword;
    }

    public Long getCashCardBatchId()
    {
        return cashCardBatchId;
    }

    public void setCashCardBatchId(Long cashCardBatchId)
    {
        this.cashCardBatchId = cashCardBatchId;
    }

    public Integer getCardStatus()
    {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus)
    {
        this.cardStatus = cardStatus;
    }

    public Long getCardIndex()
    {
        return cardIndex;
    }

    public void setCardIndex(Long cardIndex)
    {
        this.cardIndex = cardIndex;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

}
