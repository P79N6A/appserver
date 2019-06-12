package com.idcq.appserver.common.enums;
/**
 * 文件种类
 * @author zc
 *
 */
public enum FileCategory {
    /**
     * 从商铺表中取商铺LOG文件
     */
    SHOP_LOG("sl"),
    
    /**
     * 商铺表中取商铺首页图片文件
     */
    SHOP_HOME_IMAGE("sh"),
    
    /**
     * 商铺配置文件
     */
    SHOP_CONFIG_FILE("sc"),

    /**
     * 商品表中取商铺首页图片文件
     */
    GOODS_IMAGE("g");
    
    private final String code;

    private FileCategory(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    public static FileCategory getFileCategory(String code)
    {
        FileCategory re = null;
        switch (code)
        {
            case "sl":
                re = SHOP_LOG;
                break;

            case "sh":
                re = SHOP_HOME_IMAGE;
                break;
                
            case "sc":
                re = SHOP_CONFIG_FILE;
                break;
                
            case "g":
                re = GOODS_IMAGE;
                break;

            default:
                break;
        }
        return re;
    }
}
