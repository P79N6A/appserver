package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

/**
 * 商铺心跳记录表dto
 * 
 * @author Administrator
 * 
 * @date 2015年5月4日
 * @time 下午5:16:17
 */
public class HeartbeatLogDto implements Serializable{
    private static final long serialVersionUID = 3712814658720800455L;
/*    `shop_id` int(10) unsigned NOT NULL COMMENT '店铺ID',
    `system_type` tinyint(2) unsigned NOT NULL DEFAULT '1' COMMENT '系统类型:收银机=1,一点管家=2',
    `previous_time` datetime DEFAULT NULL COMMENT '上次心跳时间',
    `last_time` datetime DEFAULT NULL COMMENT '最后心跳时间',*/
    /**
     * 商铺id
     */
    private Long shopId;
    /**
     * 系统类型:收银机=1,一点管家=2
     */
    private Integer systemType;
    /**
     * 上次心跳时间
     */
    private Date previousTime;
    /**
     * 最后心跳时间
     */
    private Date lastTime;
    public Long getShopId()
    {
        return shopId;
    }
    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }
    public Integer getSystemType()
    {
        return systemType;
    }
    public void setSystemType(Integer systemType)
    {
        this.systemType = systemType;
    }
    public Date getPreviousTime()
    {
        return previousTime;
    }
    public void setPreviousTime(Date previousTime)
    {
        this.previousTime = previousTime;
    }
    public Date getLastTime()
    {
        return lastTime;
    }
    public void setLastTime(Date lastTime)
    {
        this.lastTime = lastTime;
    }
  
    
    
    
}