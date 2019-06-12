package com.idcq.appserver.common.msgPusher.baseMsg.enums;

/**
 * 该枚举代表了接受消息的客户端类型
 * @author Administrator
 *
 */
public enum MsgTargetCategory {
    /**
     * 营销短信
     */
    SMS_SEO,
    /**
     * 短信验证码
     */
    SMS_CODE,
    
    
    //以下为app推送使用
    
    /**
     * 所有android
     */
    AND_ALL,
    /**
     * 所有IOS
     */
    IOS_ALL,
    /**
     * android店铺收银机
     */
    AND_CASHIER_SHOP,
    /**
     * 一点传奇管家APP
     */
    AND_SHOP_MGR,
    /**
     * ios用户app
     */
    IOS_APP,
    /**
     * 以上全部,全部app推送，包括AND_CASHIER_SHOP, AND_SHOP_MGR, IOS_APP
     */
    ALL_PUSH,
    /**
     * 餐饮行业
     */
    RESTAURANT,

    /**
     * 餐饮行业
     */
    FASTRESTAURANT,
    /**
     * 零售行业
     */
    GOODS,
    /**
     * 服务行业
     */
    SERVICE,
    /**
     * 汽修
     */
    AUTO,
    /**
     * 其它
     */
    OTHERS;
}
