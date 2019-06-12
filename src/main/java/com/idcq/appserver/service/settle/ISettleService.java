package com.idcq.appserver.service.settle;

import java.util.Map;

import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.shop.ShopDto;

public interface ISettleService {

    void settleOrder(OrderDto order, ShopDto shop)throws Exception;
    /**
     * 
     * 
     * @Function: com.idcq.appserver.service.settle.ISettleService.userAccountSettle
     * @Description:
     *
     * @param order
     * @param configMap
     * @param userId
     * @param rebatesLevel
     * @param userRebateAmount
     * @param userBillType
     * @param refer
     * @param pushType  1首次推送，2后续推送
     */
    void userAccountSettle(OrderDto order, Map<String, Object> configMap, Long userId, String rebatesLevel,
            Double userRebateAmount, Integer userBillType, Boolean refer,boolean isPush, Integer shopIdentity) throws Exception;
}
