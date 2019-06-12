package com.idcq.appserver.dto.plugin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PluginModel implements Serializable
{
    private static final long serialVersionUID = -3437629264245149428L;

    /* 以下为主要搜索字段 */
    /*
     * 商铺Id
     */
    private Integer shopId;

    /*
     * 插件id
     */
    private Integer pluginId;

    /*
     * 基础设施:1= launcher,2=client,3=server
     */
    private Integer baseSystem;
    
    private Integer pluginType;

    /*
     * 使用场景：1= launcher,2=菜单，3=程序
     */
    private Integer functionType;

    /*
     * 使用场景：插入点id
     */
    private Integer pointId;

    /* 以下包括返回信息字段 */

    /*
     * 插件名
     */
    private String pluginName;

    /*
     * 插件最新版本
     */
    private Integer lastApprovedVersion;

    /*
     * 插件最新版本url
     */
    private String lastApprovedUrl;

    /*
     * 插件结束日期
     */
    private Date endTime;

    /*
     * 插件图标url
     */
    private String logoUrl;

    /*
     * 该版本要求的最低系统版本
     */
    private String minSysVersion;

    /*
     * 是否启用。 0:未启用 1:启用
     */
    private Integer isActive;

    /*
     * 是否允许缓存：1=允许，2不允许
     */
    private Integer allowCache;

    /*
     * 插件依赖关系
     */
    private List<Dependency> dependencies;

    /*
     * 插入点编码
     */
    private String pointCode;

    /*
     * 插入点类型
     */
    private Integer pointType;

    /*
     * 插入点接口
     */
    private String implementInterface;

    /*
     * class名字
     */
    private String pluginClass;

    /*
     * 1-Service,2-Receiver,3-Activity,4-Fragment,5-普通class
     */
    private Integer pluginCategory;

    public Integer getShopId()
    {
        return shopId;
    }

    public void setShopId(Integer shopId)
    {
        this.shopId = shopId;
    }

    public Integer getPluginId()
    {
        return pluginId;
    }

    public void setPluginId(Integer pluginId)
    {
        this.pluginId = pluginId;
    }

    public Integer getBaseSystem()
    {
        return baseSystem;
    }

    public void setBaseSystem(Integer baseSystem)
    {
        this.baseSystem = baseSystem;
    }

    public Integer getFunctionType()
    {
        return functionType;
    }

    public void setFunctionType(Integer functionType)
    {
        this.functionType = functionType;
    }

    public Integer getPointId()
    {
        return pointId;
    }

    public void setPointId(Integer pointId)
    {
        this.pointId = pointId;
    }

    public String getPluginName()
    {
        return pluginName;
    }

    public void setPluginName(String pluginName)
    {
        this.pluginName = pluginName;
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

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getLogoUrl()
    {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl)
    {
        this.logoUrl = logoUrl;
    }

    public String getMinSysVersion()
    {
        return minSysVersion;
    }

    public void setMinSysVersion(String minSysVersion)
    {
        this.minSysVersion = minSysVersion;
    }

    public Integer getIsActive()
    {
        return isActive;
    }

    public void setIsActive(Integer isActive)
    {
        this.isActive = isActive;
    }

    public Integer getAllowCache()
    {
        return allowCache;
    }

    public void setAllowCache(Integer allowCache)
    {
        this.allowCache = allowCache;
    }

    public List<Dependency> getDependencies()
    {
        return dependencies;
    }

    public void setDependencies(List<Dependency> dependencies)
    {
        this.dependencies = dependencies;
    }

    public String getPointCode()
    {
        return pointCode;
    }

    public void setPointCode(String pointCode)
    {
        this.pointCode = pointCode;
    }

    public Integer getPointType()
    {
        return pointType;
    }

    public void setPointType(Integer pointType)
    {
        this.pointType = pointType;
    }

    public String getImplementInterface()
    {
        return implementInterface;
    }

    public void setImplementInterface(String implementInterface)
    {
        this.implementInterface = implementInterface;
    }

    public String getPluginClass()
    {
        return pluginClass;
    }

    public void setPluginClass(String pluginClass)
    {
        this.pluginClass = pluginClass;
    }

    public Integer getPluginCategory()
    {
        return pluginCategory;
    }

    public void setPluginCategory(Integer pluginCategory)
    {
        this.pluginCategory = pluginCategory;
    }
    
    
    public Integer getPluginType()
    {
        return pluginType;
    }

    public void setPluginType(Integer pluginType)
    {
        this.pluginType = pluginType;
    }


    public static class Dependency
    {
        private Integer dependencyPluginId;

        private String minNum;

        public Integer getDependencyPluginId()
        {
            return dependencyPluginId;
        }

        public void setDependencyPluginId(Integer dependencyPluginId)
        {
            this.dependencyPluginId = dependencyPluginId;
        }

        public String getMinNum()
        {
            return minNum;
        }

        public void setMinNum(String minNum)
        {
            this.minNum = minNum;
        }

    }
}
