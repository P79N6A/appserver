package com.idcq.appserver.dto.common;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ConfigDto implements Serializable
{
    private static final long serialVersionUID = -9063575949010565546L;

    /**
     * 配置ID,唯一主键
     */
    @JsonIgnore
    private Long configId;

    /**
     * 配置名称
     */
    private String configKey;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 配置分组,如金额参数,配置名称,是否自动结账等
     */
    private String configGroup;

    /**
     * 配置说明
     */
    private String configDesc;

    /**
     * 对应业务表的主键，比如shop_id,user_id,bank_id,goods_id,goods_group_id,technician_id,
     * 商品分类ID,商圈活动ID,出入库ID,盘点ID,column_id
     */
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
    private Integer extendsType;

    /**
     * 是否可离线修改：1=是，0=否
     */
    private Integer isOfflineModify;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date lastUpdateTime;

    @JsonIgnore
    private Long lastUpdateUserId;
    
    /**
     * for test
     * /
/*    public static void main(String[] args) throws Exception
    {
        ConfigDto config = new ConfigDto();
        config.setBizId(123l);
        config.setConfigId(23431l);
        config.setCreateTime(new Date());
        System.out.println(JacksonUtil.objToString(config));
        
    }*/

    public Long getConfigId()
    {
        return configId;
    }

    public void setConfigId(Long configId)
    {
        this.configId = configId;
    }

    public String getConfigKey()
    {
        return configKey;
    }

    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    public String getConfigValue()
    {
        return configValue;
    }

    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getConfigGroup()
    {
        return configGroup;
    }

    public void setConfigGroup(String configGroup)
    {
        this.configGroup = configGroup;
    }

    public String getConfigDesc()
    {
        return configDesc;
    }

    public void setConfigDesc(String configDesc)
    {
        this.configDesc = configDesc;
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

    public Integer getExtendsType()
    {
        return extendsType;
    }

    public void setExtendsType(Integer extendsType)
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

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Long getLastUpdateUserId()
    {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(Long lastUpdateUserId)
    {
        this.lastUpdateUserId = lastUpdateUserId;
    }
    /**
     * 这里采用弱相等，即{@link ConfigDto.configKey} 相等则认为相等
     */
    @Override
    public  boolean equals(Object obj){
        boolean result = false;
        if(null != obj){
            ConfigDto dto = (ConfigDto)obj;
            if(dto.getConfigKey().equals(this.getConfigKey())){
                result = true;
            }
        }
        return result;
    }
    
    @Override
    public int hashCode()
    {   
        return this.configKey.hashCode();
    }
    
}
