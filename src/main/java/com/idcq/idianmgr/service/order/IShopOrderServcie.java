package com.idcq.idianmgr.service.order;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.idianmgr.dto.order.OfflineOrderDto;
import com.idcq.idianmgr.dto.order.ShopOrderDetailDto;

/**
 * 商铺相关订单接口
 * @author shengzhipeng
 * @date:2015年7月30日 下午2:03:58
 */
public interface IShopOrderServcie {

	/**
	 * 
	 * 
	 * @Function: com.idcq.idianmgr.service.order.IShopOrderServcie.getShopOrders
	 * @Description:
	 *
	 * @param map
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:szp
	 * @date:2015年7月30日 下午2:07:15
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日   shengzhipeng       v1.0.0         create
	 */
	PageModel getShopOrders(Map map, int page, int pageSize) throws Exception;
	
	/**
	 * 根据shopId统计该店铺各种订单状态的数量
	 * @Function: com.idcq.idianmgr.service.order.IShopOrderServcie.getShopOrdersNumber
	 * @Description:
	 *
	 * @param shopId 店铺id
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月30日 下午4:13:11
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    shengzhipeng       v1.0.0         create
	 */
	List<Map<String, Object>> getShopOrdersNumber(Long shopId) throws Exception;
	
	/**
	 * 
	 * 操作订单接口
	 * @Function: com.idcq.idianmgr.service.order.IShopOrderServcie.updateOrderStatus
	 * @Description:主要对订单状态进行更改，需要记录订单状态变更日志
	 *
	 * @param userId
	 * @param orderId
	 * @param operateType
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月30日 下午4:51:05
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    shengzhipeng       v1.0.0         create
	 */
	Map updateOrderStatus(Long userId, String orderId, String operateType) throws Exception;
	
	/**
	 * 获取订单详情
	 * @return
	 * @throws Exception
	 */
	ShopOrderDetailDto getShopOrderDetailById(String orderId) throws Exception;
	
	/**
	 * 获取商铺线下订单列表，默认查询多少天内未完成的订单
	 * 
	 * @Function: com.idcq.idianmgr.service.order.IShopOrderServcie.getShopOfflineOrders
	 * @Description:
	 *
	 * @param shopId
	 * @param day
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月31日 上午9:31:56
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    shengzhipeng       v1.0.0         create
	 */
	List<Map> getShopOfflineOrders(Long shopId, Integer day) throws Exception;
	
	/**
	 * 商家线下下订单（场地类） 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	String OfflinePlaceOrder(OfflineOrderDto order) throws Exception;
	
	/**
	 * 商家取消线下订单
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int cancelOfflineOrder(HttpServletRequest request) throws Exception;
	
	/**
	 * 获取商铺分类下的订单接口
	 * 
	 * @Function: com.idcq.idianmgr.service.order.IShopOrderServcie.getShopCategoryOrders
	 * @Description:
	 *
	 * @param shopId
	 * @param categoryId
	 * @param dates
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年7月31日 下午4:03:53
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    shengzhipeng       v1.0.0         create
	 */
	List<Map> getShopCategoryOrders(Long shopId, Long categoryId, String dates) throws Exception;
	
}
