package com.idcq.appserver.dao.shop;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.shop.ShopConfigureSettingDto;
import com.idcq.appserver.utils.DateUtils;

/**
 * 这是一个兼容旧版本操作配置的转换器(适配器)
 * @Date 2016-05-26
 * @author zc
 * @deprecated
 *
 */
public class ConfigTypeConvertor
{   
    private static final Logger log = LoggerFactory.getLogger(ConfigTypeConvertor.class);
    
    /**
     * 将商铺配置的settingType转换成对应的configGroup
     * @param settingType
     * @return
     */
    public static String convertShopSettingForConfigGroup(Integer settingType)
    {
        String result = null;
        if (settingType != null)
        {
            switch (settingType)
            {
                case 1:
                    result = "POINT_CONFIG";
                    break;
                case 2:
                    result = "PRINT_CONFIG";
                    break;
                case 3:
                    result = "DEPOSIT_CONFIG";
                    break;
                default:
                    throw new IllegalArgumentException("不受支持的参数settingType：" + settingType);
            }
        }
        return result;
    }
    
    public static Integer convertConfigGroupForShopSetting(String configGroup){
        Integer result = null;
        if (configGroup != null)
        {
            switch (configGroup)
            {
                case "POINT_CONFIG":
                    result = 1;
                    break;
                case "PRINT_CONFIG":
                    result = 2;
                    break;
                case "DEPOSIT_CONFIG":
                    result = 3;
                    break;
                default:
                    throw new IllegalArgumentException("不受支持的参数：" + configGroup);
            }
        }
        return result;
    }
    
    public static String convertSysOldConfigureTypeForNew(Integer congigureType){
        String result = null;
        if (congigureType != null)
        {
            switch (congigureType)
            {
                case 1:
                    result = "MONEY_PARAM";
                    break;
                case 2:
                    result = "CONFIG_NAME";
                    break;
                case 3:
                    result = "AUTO_SETTLE";
                    break;
                default:
                    throw new IllegalArgumentException("不受支持的参数：" + congigureType);
            }
        }
        return result;
    }
    
    public static Integer convertForOldSysConfigureType(String configGroup){
        Integer result = null;
        if (configGroup != null)
        {
            switch (configGroup)
            {
                case "MONEY_PARAM":
                    result = 1;
                    break;
                case "CONFIG_NAME":
                    result = 2;
                    break;
                case "AUTO_SETTLE":
                    result = 3;
                    break;
                case "OTHERS":
                    break;
                default:
                    throw new IllegalArgumentException("不受支持的参数：" + configGroup);
            }
        }
        return result;
    }
    
    /**
     * 在两个实体间进行转换
     * @param shopConfigureSettingDto
     * @return
     */
    public static ConfigDto convertShopConfigureSettingDtoToConfigDto(ShopConfigureSettingDto shopConfigureSettingDto)
    {
        ConfigDto result = new ConfigDto();
        result.setConfigId(shopConfigureSettingDto.getSettingId());
        if (null != shopConfigureSettingDto.getShopId())
        {
            result.setBizType(1);
            result.setBizId(shopConfigureSettingDto.getShopId());
        }
        result.setConfigDesc(shopConfigureSettingDto.getSettingDesc());
        result.setConfigKey(shopConfigureSettingDto.getSettingKey());
        result.setConfigGroup(convertShopSettingForConfigGroup(shopConfigureSettingDto.getSettingType()));
        result.setConfigValue(shopConfigureSettingDto.getSettingValue());
        String date = shopConfigureSettingDto.getCreateTime();
        if(null != date){
            result.setCreateTime(DateUtils.parse(date));
        }
        result.setExtendsType(0);
        result.setIsOfflineModify(1);
        String lastUpdateTime = shopConfigureSettingDto.getLastUpdateTime();
        if(null != lastUpdateTime){
            result.setLastUpdateTime(DateUtils.parse(lastUpdateTime));
        }
        return result;
    }
    
    public static ShopConfigureSettingDto convertConfigDtoToShopConfigureSettingDto(ConfigDto configDto){
        ShopConfigureSettingDto result = new ShopConfigureSettingDto();
        Date date = configDto.getCreateTime();
        if(null != date){
            try
            {
                result.setCreateTime(DateUtils.getTimeStrByPattern(date, "yyyy-MM-dd HH:mm:ss"));
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
        }
        Date lastUpdateTime = configDto.getCreateTime();
        if(null != date){
            try
            {
                result.setLastUpdateTime(DateUtils.getTimeStrByPattern(lastUpdateTime, "yyyy-MM-dd HH:mm:ss"));
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
        }
        result.setSettingDesc(configDto.getConfigDesc());
        result.setSettingId(configDto.getConfigId());
        result.setSettingKey(configDto.getConfigKey());
        result.setSettingValue(configDto.getConfigValue());
        result.setShopId(configDto.getBizId());
        result.setSettingType(convertConfigGroupForShopSetting(configDto.getConfigGroup()));
        return result;
    }
}
