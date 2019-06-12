package com.idcq.appserver.service.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopTechnicianDto;
import com.idcq.appserver.dto.shop.ShopTechnicianRefGoodsDto;

public interface IShopTechnicianService {
	/**
	 * 查询技师存在性
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int queryTechnicianExists(Long id) throws Exception;
	
	/**
	 *  获取指定技师名称
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopTechnicianService.
	 *            getTechnicianName
	 * @Description:
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 * 
	 * @version:v1.0
	 * @author:LuJianPing
	 * @date:2015年7月30日 下午2:31:26
 	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月5日    LuJianPing      v1.0.0         create
	 */
	String getTechnicianName(Long id) throws Exception;

	/**
	 * 增加/更新技师
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopTechnicianService.
	 *            insertShopTechnician
	 * @Description:
	 * 
	 * @param shopTechnicianDto
	 * @return
	 * @throws Exception
	 * 
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午2:31:26
	 * 
	 *                  Modification History: Date Author Version Description
	 *                  ----
	 *                  ------------------------------------------------------
	 *                  ------- 2015年7月30日 ChenYongxin v1.0.0 create
	 */
	Long insertAndUpdateShopTechnician(ShopTechnicianDto shopTechnicianDto)
			throws Exception;

	/**
	 * 删除技师
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopTechnicianService.
	 *            deleteShopTechnician
	 * @Description:
	 * 
	 * @param shopTechnicianDto
	 * @return
	 * @throws Exception
	 * 
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午2:31:53
	 * 
	 *                  Modification History: Date Author Version Description
	 *                  ----
	 *                  ------------------------------------------------------
	 *                  ------- 2015年7月30日 ChenYongxin v1.0.0 create
	 */
	void deleteShopTechnician(Long shopId, String techIds) throws Exception;

	/**
	 * 删除商品族和技师关系
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopTechnicianService.
	 *            deleteShopTechRefGoods
	 * @Description:
	 * 
	 * @param shopTechnicianRefGoodsDto
	 * @return
	 * @throws Exception
	 * 
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午7:32:13
	 * 
	 *                  Modification History: Date Author Version Description
	 *                  ----
	 *                  ------------------------------------------------------
	 *                  ------- 2015年7月30日 ChenYongxin v1.0.0 create
	 */
	Integer deleteShopTechRefGoods(
			ShopTechnicianRefGoodsDto shopTechnicianRefGoodsDto)
			throws Exception;

	/**
	 * 获取技师服务项目列表
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopTechnicianService.
	 *            getTechnicianServiceItems
	 * @Description:
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 * 
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 上午9:58:39
	 * 
	 *                  Modification History: Date Author Version Description
	 *                  ----
	 *                  ------------------------------------------------------
	 *                  ------- 2015年7月31日 ChenYongxin v1.0.0 create
	 */
	public PageModel getTechnicianServiceItems(Map<String, Object> map)
			throws Exception;

	/**
	 * 获取商品技师接单
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopTechnicianService.
	 *            getScheduleSetting
	 * @Description:
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 * 
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 上午11:48:57
	 * 
	 *                  Modification History: Date Author Version Description
	 *                  ----
	 *                  ------------------------------------------------------
	 *                  ------- 2015年7月31日 ChenYongxin v1.0.0 create
	 */
	public List<Map<String, Object>> getScheduleSetting(Map<String, Object> map)
			throws Exception;
	/**
	 * 获取商铺技师
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopTechnicianService.getTechnicianList
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 下午2:56:22
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	public PageModel getTechnicianList(Map<String, Object> map)
			throws Exception;
	
	/**
	 * 更新技师工作状态
	 * @param techIds
	 * @param workStatus
	 * @return
	 * @throws Exception
	 */
	public int updateTechnicianWorkStatus(List<Long> techIds,int workStatus) throws Exception;
	
	/**
	 * 查询当天排班为休息的技师Id列表
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Long> getTechIdList(Map<String,Object> param) throws Exception;
	
	/**
	 * 将当前订单绑定的技师的状态置为workStatus
	 * @param orderId
	 * @param workStatus
	 * @return
	 * @throws Exception
	 */
	public int updateTechnicianWorKStatusByOrderId(String orderId,int workStatus)throws Exception;
	/**
	 * 
	 * 获取技师订单数
	 * @Function: com.idcq.appserver.service.shop.IShopTechnicianService.getTechnicianOrderNum
	 * @Description:
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月31日 下午3:44:39
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月31日    ChenYongxin      v1.0.0         create
	 */
	Map<String, Object> getTechnicianOrderNum(Map<String, Object> param) throws Exception;
	
	Integer validateTechExit(Map<String, Object> map) throws Exception;
}
