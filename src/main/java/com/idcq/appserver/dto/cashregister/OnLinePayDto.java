package com.idcq.appserver.dto.cashregister;

import java.io.Serializable;

/**
 * 收银机一点传奇支付dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月12日
 * @time 上午10:17:34
 */
public class OnLinePayDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = -1306235811755509282L;

    private String token;

    private Long shopId;

    private String mobile;

    private DataDto data;

    /**
     * 确认类型：0-传奇宝支付；1-短信通知支付
     */
    private Integer confirmType;

    /**
     * 短信验证码
     */
    private String veriCode;

    public OnLinePayDto()
    {
        super();
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public DataDto getData()
    {
        return data;
    }

    public void setData(DataDto data)
    {
        this.data = data;
    }

    public Integer getConfirmType()
    {
        return confirmType;
    }

    public void setConfirmType(Integer confirmType)
    {
        this.confirmType = confirmType;
    }

    public String getVeriCode()
    {
        return veriCode;
    }

    public void setVeriCode(String veriCode)
    {
        this.veriCode = veriCode;
    }
}