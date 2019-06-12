package com.idcq.appserver.dto.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigQueryCondition implements Cloneable
{  
    private static final Logger log = LoggerFactory.getLogger(ConfigQueryCondition.class);
    
    private Long shopId;
    
    private Long configId;
    
    private String[] configKeys;
    
    private String[] configGroups;
    
    private Long bizId;
    
    /**
     * 业务主键类型，商铺=1,用户=2,模板=3,用户服务协议=4,商家服务协议=5,代金券=6,银行=7,商品=8,商品族=9,技师=10,商品分类=
     * 11,launcher主页图标=12,商圈活动=13,收银机日志=14,商圈活动类型=15,消息中心消息=16,出入库记录=17,盘点记录=18,
     * 栏目=19
     */
    private Integer bizType;
    
    /**
     * 继承类型：0=不可继承，1=可继承，2=可继承和修改
     */
    private List<Integer> extendsType;

    /**
     * 是否可离线修改：1=是，0=否
     */
    private Integer isOfflineModify;
    
    /**
     * 排序：1:按照config_key排序
     */
    private Integer orderBy;
    
    /**
     * 排序方式：倒序-0；顺序-1
     */
    private Integer orderByMode;

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Long getConfigId()
    {
        return configId;
    }

    public void setConfigId(Long configId)
    {
        this.configId = configId;
    }

    public String[] getConfigKeys()
    {
        return configKeys;
    }

    public void setConfigKeys(String[] configKeys)
    {
        this.configKeys = configKeys;
    }

    public String[] getConfigGroups()
    {
        return configGroups;
    }

    public void setConfigGroups(String[] configGroups)
    {
        this.configGroups = configGroups;
    }

    public Long getBizId()
    {
        return bizId;
    }

    public void setBizId(Long bizId)
    {
        this.bizId = bizId;
    }

    public Integer getBizType()
    {
        return bizType;
    }

    public void setBizType(Integer bizType)
    {
        this.bizType = bizType;
    }

    public List<Integer> getExtendsType()
    {
        return extendsType;
    }

    public void setExtendsType(List<Integer> extendsType)
    {
        this.extendsType = extendsType;
    }

    public Integer getIsOfflineModify()
    {
        return isOfflineModify;
    }

    public void setIsOfflineModify(Integer isOfflineModify)
    {
        this.isOfflineModify = isOfflineModify;
    }
    
    public ConfigQueryCondition getCopy(){
        ConfigQueryCondition result = null;
        try
        {
            result = (ConfigQueryCondition)this.clone();
        }
        catch (CloneNotSupportedException e)
        {
            log.error(e.getMessage(), e);
        }
        return result;
    }

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getOrderByMode() {
		return orderByMode;
	}

	public void setOrderByMode(Integer orderByMode) {
		this.orderByMode = orderByMode;
	}
    
    
    
}
