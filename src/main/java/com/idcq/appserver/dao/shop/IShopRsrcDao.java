package com.idcq.appserver.dao.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopRsrcDto;
import com.idcq.appserver.dto.shop.ShopRsrcPramDto;

public interface IShopRsrcDao {
	
	ShopRsrcDto getShopRsrcById(Long resourceId) throws Exception;
	
	/**
	 * 获取指定商铺资源所属商品分类ID
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	Long getCategoryIdByRsrId(Long resourceId) throws Exception;
	
	/**
	 * 获取指定商铺资源所属商品分类ID和资源名称
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getCategoryIdAndRsrNameByRsrId(Long resourceId) throws Exception;
	
	/**
	 * 用户使用商铺资源
	 * @param param
	 * @throws Exception
	 */
	public int useShopResource(Map param) throws Exception ;
	
	List<Map> getShopRsrcList(Long shopId,Long groupId) throws Exception;
	
	/**
	 * 更新商铺资源状态
	 * @param param
	 * @throws Exception
	 */
	public int updateShopResource(ShopRsrcDto shopRsrcDto) throws Exception ;
	
	/**
	 * 获取指定预订状态的资源列表
	 * @param shopRsrcDto
	 * @return
	 * @throws Exception
	 */
	List<ShopRsrcDto> getShopResourceList(ShopRsrcDto shopRsrcDto) throws Exception;
	
	public List<Map> getShopGroupResourceList(Long shopId);
	/**
	 * 场地查询商铺资源
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopRsrcDao.getShopCategoryResource
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 下午7:35:47
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>>getShopCategoryResource(Map<String, Object> map) throws Exception;
	/**
	 * 插入商铺资源
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopRsrcDao.insertShopCategoryResource
	 * @Description:
	 *
	 * @param shopRsrcDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月1日 上午11:33:43
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月1日    ChenYongxin      v1.0.0         create
	 */
	Integer insertShopCategoryResource(ShopRsrcPramDto shopRsrcPramDto)throws Exception;
	/**
	 * 更新商铺资源
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopRsrcDao.updateShopCategoryResource
	 * @Description:
	 *
	 * @param shopRsrcDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月1日 上午11:34:04
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月1日    ChenYongxin      v1.0.0         create
	 */
	Integer updateShopCategoryResource(ShopRsrcPramDto shopRsrcPramDto)throws Exception;
	/**
	 * 删除商品资源
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopRsrcDao.deleteShopCategoryResource
	 * @Description:
	 *
	 * @param shopRsrcDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月1日 上午11:34:17
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月1日    ChenYongxin      v1.0.0         create
	 */
	Integer deleteShopCategoryResource(ShopRsrcPramDto shopRsrcPramDto)throws Exception;
	/**
	 * 查询指定商铺资源是否存在
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	int queryShopResourceExists(Long resourceId) throws Exception;
	/**
	 * 查询商铺资源存在性
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopRsrcDao.queryResourceExists
	 * @Description:
	 *
	 * @param resourceId
	 * 
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:LuJianPing
	 * @date:2015年8月4日 上午10:26:30
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月4日    LuJianPing      v1.0.0         create
	 */
	int queryResourceExists(Long resourceId) throws Exception;
	
	/**
	 * 查询指定商铺资源名称
	 * @param resourceId
	 * @return
	 * @throws Exception
	 */
	String getShopResourceName(Long resourceId) throws Exception;
	
	List<String> getSeatNameBySeatIds(String[] seatIds);
}
