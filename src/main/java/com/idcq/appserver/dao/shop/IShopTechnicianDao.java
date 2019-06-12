package com.idcq.appserver.dao.shop;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopTechnicianDto;
import com.idcq.appserver.dto.shop.ShopTechnicianRefGoodsDto;

public interface IShopTechnicianDao {

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
	 * 增加技师
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.insertShopTechnician
	 * @Description:
	 *
	 * @param shopTechnicianDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午2:01:00
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    ChenYongxin      v1.0.0         create
	 */
	Integer insertShopTechnician(ShopTechnicianDto shopTechnicianDto) throws Exception;
	/**
	 * 更新技师
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.updateShopTechnician
	 * @Description:
	 *
	 * @param shopTechnicianDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午2:02:16
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    ChenYongxin      v1.0.0         create
	 */
	Integer updateShopTechnician(ShopTechnicianDto shopTechnicianDto) throws Exception;
	/**
	 * 删除技师
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.deleteShopTechnician
	 * @Description:
	 *
	 * @param shopTechnicianDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午2:10:49
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    ChenYongxin      v1.0.0         create
	 */
	Integer deleteShopTechnician(ShopTechnicianDto shopTechnicianDto) throws Exception;
	/**
	 * 批量删除技师
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.deleteBatcheShopTechRefGoods
	 * @Description:
	 *
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午8:20:18
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    ChenYongxin      v1.0.0         create
	 */
	Integer deleteBatchShopTechnician(Map<String,Object> map) throws Exception;
	/**
	 * 批量插入技师商品族关联关系
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.insertShopTechRefGoods
	 * @Description:
	 *
	 * @param list
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午5:03:01
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    ChenYongxin      v1.0.0         create
	 */
	Integer insertShopTechRefGoods(List<ShopTechnicianRefGoodsDto> list)
			throws Exception;
	/**
	 * 批量删除技师商品族关联关系
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.deleteShopTechRefGoods
	 * @Description:
	 *
	 * @param list
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午5:42:26
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    ChenYongxin      v1.0.0         create
	 */
	Integer updateShopTechRefGoods(ShopTechnicianRefGoodsDto shopTechnicianRefGoodsDto)
			throws Exception;
	/**
	 * 删除商品族和技师关联关系
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.deleteShopTechRefGoods
	 * @Description:
	 *
	 * @param shopTechnicianRefGoodsDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午7:31:28
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    ChenYongxin      v1.0.0         create
	 */
	Integer deleteShopTechRefGoods(ShopTechnicianRefGoodsDto shopTechnicianRefGoodsDto)
			throws Exception;
	/**
	 * 批量删除商品族和技师关联关系
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.deletBatcheShopTechRefGo1ods
	 * @Description:
	 *
	 * @param list
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月30日 下午7:43:47
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月30日    ChenYongxin      v1.0.0         create
	 */
	public Integer deleteBatcheShopTechRefGoods(List<Long> techIds)throws Exception;
			
	/**
	 * 获取技师服务列表
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getTechnicianServiceItems
	 * @Description:
	 *
	 * @param map
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 上午10:00:10
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>>getTechnicianServiceItems(Map<String, Object> map)throws Exception;
	/**
	 * 获取技师服务总记录数
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getTechnicianServiceItemsCount
	 * @Description:
	 *
	 * @param map
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 上午10:00:49
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	Integer getTechnicianServiceItemsCount(Map<String, Object> map)throws Exception;
	/**
	 * 获取商铺技师接单列表
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getScheduleSetting
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 上午11:47:29
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getScheduleSetting(Map<String, Object> map)throws Exception;
	/**
	 * 
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.updateBatchShopTechnician
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 下午1:38:47
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	Integer updateBatchShopTechnician(Map<String, Object> map)throws Exception;
	/**
	 * 获取接口list
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getTechnicianList
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 下午2:49:43
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getTechnicianList(Map<String, Object> map)throws Exception;
	/**
	 * 获取商铺技师列表数
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getTechnicianListCount
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 下午2:54:14
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	Integer getTechnicianListCount(Map<String, Object> map)throws Exception;
	/**
	 * 根据时间查询班次类型
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getClassesTypeByTime
	 * @Description:
	 *
	 * @param date
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 下午4:13:08
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	Integer  getClassesTypeByTime(String dateStr,Long shopId)throws Exception;
	/**
	 * 商铺技师服务项目
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getTechServiceCount
	 * @Description:
	 *
	 * @param techId
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 下午4:43:53
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	Integer getTechServiceCount (Long techId)throws Exception;
	/**
	 * 获取技师工作时间
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getTechWorkTimeByMap
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月6日 下午5:34:07
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月6日    ChenYongxin      v1.0.0         create
	 */
	String getTechWorkTimeByMap(Map<String, Object> map)throws Exception;
	/**
	 * 获取接单总记录数
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getScheduleSettingCount
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月7日 上午10:56:20
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月7日    ChenYongxin      v1.0.0         create
	 */
	Integer getScheduleSettingCount(Map<String, Object> map)throws Exception;
	/**
	 * 统计技师的接单数量
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.statisTechnicianOrderNumExecute
	 * @Description:
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:LuJianPing
	 * @date:2015年8月11日 上午10:28:20
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月11日   LuJianPing      v1.0.0         create
	 */
	int statisTechnicianOrderNumExecute(Date startTime,Date endTime) throws Exception;
	
	/**
	 * 批量更新技师工作状态
	 * @param techIds
	 * @return
	 * @throws Exception
	 */
	int updateTechnicianWorkStatus(Map<String, Object> param) throws Exception;
	
	/**
	 * 查询当天排班为休息的技师ID列表
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<Long> getTechIdList(Map<String, Object> param) throws Exception;
	/**
	 * 获取技师接单数
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getTechnicianOrderNum
	 * @Description:
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月31日 下午3:40:19
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月31日    ChenYongxin      v1.0.0         create
	 */
	Map<String, Object> getTechnicianOrderNum(Map<String, Object> param) throws Exception;
	/**
	 * 根据技师id获取技师
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopTechnicianDao.getTechWorkStatusById
	 * @Description:
	 *
	 * @param technicianId
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月29日 上午10:29:12
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月29日    ChenYongxin      v1.0.0         create
	 */
	Integer getTechWorkStatusById(Long technicianId) throws Exception;
	
	Integer validateTechExit(Map<String, Object> map) throws Exception;
	
	public List<ShopTechnicianDto> getGoodTechs(String goodsId); 
 }
