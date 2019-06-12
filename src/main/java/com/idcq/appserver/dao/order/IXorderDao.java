package com.idcq.appserver.dao.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.order.XorderDto;

public interface IXorderDao {
	/**
	 * 新增非会员订单
	 * 
	 * @param xorderDto
	 * @return
	 * @throws Exception
	 */
	int addXorderDto(XorderDto xorderDto) throws Exception;
	
	/**
	 * 删除指定非会员订单
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int delXorderDto(String orderId) throws Exception;
	
	int updateXorderDto(XorderDto xorderDto) throws Exception;
	
	Integer getOrderStatusById(String orderId) throws Exception;
	
	/**
	 * 查询非会员订单存在性
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int queryXorderExists(String orderId) throws Exception;
	
	/**
	 * 取消订单
	 * <p>订单状态置为已退订
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int canclXorder(String orderId) throws Exception;
	
	/**
	 * 获取非会员订单简单信息
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	XorderDto getXorderSimple(String orderId) throws Exception;
	
    XorderDto getXOrderById(String orderId) throws Exception;
    
    /**
     * 获取商铺线下预定列表
     * 
     * @Function: com.idcq.appserver.dao.order.IXorderDao.getShopOfflineOrders
     * @Description:查询多少天内未完成的订单
     *
     * @param shopId
     * @param day
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:shengzhipeng
     * @date:2015年7月31日 上午9:34:24
     *
     * Modification History:
     * Date            Author       Version     Description
     * -----------------------------------------------------------------
     * 2015年7月31日    shengzhipeng       v1.0.0         create
     */
    List<Map> getShopOfflineOrders(Long shopId, Integer day) throws Exception;
    
    String getGoodsLogoByXorderId(String xOrderId) throws Exception;
    
    /**
     * 获取非会员订单需要支付的金额
     * @param xorderId
     * @return
     * @throws Exception
     */
	BigDecimal getSettingPriceByxorderId(String xorderId) throws Exception;
}
