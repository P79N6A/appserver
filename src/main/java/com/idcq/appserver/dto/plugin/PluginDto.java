/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.plugin.PluginDto
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年3月1日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.plugin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PluginDto implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 6474298322144068005L;
    /**
     * 主键
     */
    @JsonIgnore
    private Integer id;
    /**
     * 插件名称
     */
    private String pluginName;
    /**
     * 插件备注
     */
    private String pluginDesc;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 插件应用行业
     */
    private String columnId;
    /**
     * 最新版本
     */
    private Integer lastApprovedVersion;
    /**
     * 路径
     */
    private String lastApprovedUrl;
    /**
     * 计费方式
     */
    private Integer charge_way;
    /**
     * 免费计数
     */
    private Integer free_value;
    /**
     * 计费金额
     */
    private BigDecimal  money;
    /**
     * 开发者
     */
    private Integer developer_user_id;
    /**
     * 路径
     */
    private BigDecimal developer_rate;
    /**
     * 路径
     */
    private Integer status;
    
    private Date createTime;
    public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public String getPluginName()
    {
        return pluginName;
    }
    public void setPluginName(String pluginName)
    {
        this.pluginName = pluginName;
    }
    public String getPluginDesc()
    {
        return pluginDesc;
    }
    public void setPluginDesc(String pluginDesc)
    {
        this.pluginDesc = pluginDesc;
    }
    public String getPackageName()
    {
        return packageName;
    }
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
    public String getColumnId()
    {
        return columnId;
    }
    public void setColumnId(String columnId)
    {
        this.columnId = columnId;
    }
    public Integer getLastApprovedVersion()
    {
        return lastApprovedVersion;
    }
    public void setLastApprovedVersion(Integer lastApprovedVersion)
    {
        this.lastApprovedVersion = lastApprovedVersion;
    }
    public String getLastApprovedUrl()
    {
        return lastApprovedUrl;
    }
    public void setLastApprovedUrl(String lastApprovedUrl)
    {
        this.lastApprovedUrl = lastApprovedUrl;
    }
    public Integer getCharge_way()
    {
        return charge_way;
    }
    public void setCharge_way(Integer charge_way)
    {
        this.charge_way = charge_way;
    }
    public Integer getFree_value()
    {
        return free_value;
    }
    public void setFree_value(Integer free_value)
    {
        this.free_value = free_value;
    }
    public BigDecimal getMoney()
    {
        return money;
    }
    public void setMoney(BigDecimal money)
    {
        this.money = money;
    }
    public Integer getDeveloper_user_id()
    {
        return developer_user_id;
    }
    public void setDeveloper_user_id(Integer developer_user_id)
    {
        this.developer_user_id = developer_user_id;
    }
    public BigDecimal getDeveloper_rate()
    {
        return developer_rate;
    }
    public void setDeveloper_rate(BigDecimal developer_rate)
    {
        this.developer_rate = developer_rate;
    }
    public Integer getStatus()
    {
        return status;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    
    
}
