package com.idcq.idianmgr.dao.order;

import java.util.List;
import java.util.Map;

import com.idcq.idianmgr.dto.order.ShopOrderDto;

/**
 * 商铺订单dao层接口
 * @author shengzhipeng
 * @date:2015年7月30日 下午2:18:55
 */
public interface IShopOrderDao {

	/**
	 * 根据商铺id获取
	 * @Function: com.idcq.idianmgr.dao.order.IShopOrderDao.getShopOrders
	 * @Description:
	 *
	 * @param map
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月30日 下午2:21:47
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    shengzhipeng       v1.0.0         create
	 */
	List<ShopOrderDto> getShopOrders(Map map, int page, int pageSize) throws Exception;
	 
	/**
	 * 
	 * 
	 * @Function: com.idcq.idianmgr.dao.order.IShopOrderDao.getShopOrdersCount
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月30日 下午2:29:26
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    shengzhipeng       v1.0.0         create
	 */
	int getShopOrdersCount(Map map) throws Exception;
	
	/**
	 * 根据shopId统计该店铺各种订单状态的数量
	 * @Function: com.idcq.idianmgr.dao.order.IShopOrderDao.getShopOrdersNumber
	 * @Description:
	 *
	 * @param shopId 店铺id
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月30日 下午4:10:46
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    shengzhipeng       v1.0.0         create
	 */
	List<Map<String, Object>> getShopOrdersNumber(Long shopId) throws Exception;
	
	/**
	 * 
	 * 根据订单状态获取对应店铺下未支付的订单个数
	 * @Function: com.idcq.idianmgr.dao.order.IShopOrderDao.getFiterShopOrderNum
	 * @Description:
	 *
	 * @param shopId
	 * @param orderStatus
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月30日 下午4:30:35
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    shengzhipeng       v1.0.0         create
	 */
	int getNotPayOrderNum(Long shopId, Integer orderStatus) throws Exception;
	
	/**
	 * 根据分类获取订单列表
	 * @Function: com.idcq.idianmgr.dao.order.IShopOrderDao.getShopCategoryOrders
	 * @Description:
	 *
	 * @param shopId
	 * @param categoryId
	 * @param date
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月31日 下午4:14:43
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    shengzhipeng       v1.0.0         create
	 */
	List<Map> getShopCategoryOrders(Long shopId, Long categoryId, String[] date);
	
	/**
	 * 获取订单详情
	 * @return
	 * @throws Exception
	 */
	ShopOrderDto getShopOrderById(String orderId) throws Exception;
	
	
	String getGoodsLogoByOrderId(String orderId) throws Exception;
	
}
