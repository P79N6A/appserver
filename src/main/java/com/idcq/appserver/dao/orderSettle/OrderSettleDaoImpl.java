package com.idcq.appserver.dao.orderSettle;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.order.OrderSettleDto;
/**
 * 我的订单dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:08:53
 */
@Repository
public class OrderSettleDaoImpl extends BaseDao<OrderSettleDto>implements IOrderSettleDao{

    @Override
    public void saveOrderSettle(OrderSettleDto orderSettle) throws Exception {
        super.insert(generateStatement("saveOrderSettle"), orderSettle);
        
    }
    @Override
    public int updateOrderSettle(Map map) throws Exception {
        int flag = super.update(generateStatement("updateOrderSettle"), map);
        return flag ;
    }
}
