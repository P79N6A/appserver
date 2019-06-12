/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.utils.mq.goods.GoodsMessageUtil
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年1月4日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.utils.mq.goods;

import java.util.List;

import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.mq.MqProduceApi;
/**
 * 商品MQ消息推送工具类
 * @author ChenYongxin
 *
 */
public class GoodsMessageUtil
{
    /**
     * 商品变更消息推送
     * 
     * @Function: com.idcq.appserver.utils.mq.goods.GoodsMessageUtil.pushGoodsMessage
     * @Description:
     *
     * @param goodsIds
     * @param shopId
     * @param operateType
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年1月4日 下午4:25:43
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年1月4日    ChenYongxin      v1.0.0         create
     */
    public static void pushGoodsMessage(List<Long> goodsIds,Long shopId,Integer operateType) throws Exception{
        IShopServcie shopService  = BeanFactory.getBean(IShopServcie.class);
        //获取商铺
        ShopDto shop = shopService.getShopById(shopId);
        Integer columnId = null;
        if(shop!=null){
           columnId =  shop.getColumnId();
        }

        //发送消息
        for (Long goodId : goodsIds) {
            StringBuilder content = new StringBuilder();
            content.append("{");
            content.append("\"id\":\""+goodId+"\",");
            content.append("\"serverLastTime\":\"" + System.currentTimeMillis() + "\",");
            content.append("\"fields\":{");
            content.append("\"status\":\""+operateType+"\"}");
            content.append("}");
            MqProduceApi.setMessage("ChangedNotifyServer"+columnId, "goods", content.toString());
        }

    }
}
