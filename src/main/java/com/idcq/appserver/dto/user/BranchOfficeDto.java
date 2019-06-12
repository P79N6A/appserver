package com.idcq.appserver.dto.user;

import java.util.Date;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class BranchOfficeDto
{
    //分公司ID
    Long branchOfficeId;
    //会员ID
    Long userId;
    //手机号
    Long mobile;
    //所处的省ID
    Long provinceId;
    //市ID
    Long cityId;
    //区、县ID
    Long districtId;
    //镇、街道ID
    Long townId;
    //创建时间
    Date createTime;
    //总返还管理费金额
    Double manageFeeTotal;

    public Long getBranchOfficeId()
    {
        return branchOfficeId;
    }

    public void setBranchOfficeId(Long branchOfficeId)
    {
        this.branchOfficeId = branchOfficeId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getMobile()
    {
        return mobile;
    }

    public void setMobile(Long mobile)
    {
        this.mobile = mobile;
    }

    public Long getProvinceId()
    {
        return provinceId;
    }

    public void setProvinceId(Long provinceId)
    {
        this.provinceId = provinceId;
    }

    public Long getCityId()
    {
        return cityId;
    }

    public void setCityId(Long cityId)
    {
        this.cityId = cityId;
    }

    public Long getDistrictId()
    {
        return districtId;
    }

    public void setDistrictId(Long districtId)
    {
        this.districtId = districtId;
    }

    public Long getTownId()
    {
        return townId;
    }

    public void setTownId(Long townId)
    {
        this.townId = townId;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Double getManageFeeTotal()
    {
        return manageFeeTotal;
    }

    public void setManageFeeTotal(Double manageFeeTotal)
    {
        this.manageFeeTotal = manageFeeTotal;
    }
}
