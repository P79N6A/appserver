package com.idcq.appserver.service.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopRsrcPramDto;

/**
 * 商品资源service
 * @author ChenYongxin
 *
 */
public interface IShopRsrcServcie {
	/**
	 * 获取商铺资源场地类
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopRsrcServcie.getShopCategoryResource
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年7月31日 下午7:52:36
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年7月31日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>>getShopCategoryResource(Map<String, Object> map) throws Exception;
	/**
	 * 增加、删除、更新场地商品资源信息
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopRsrcServcie.operateResource
	 * @Description:
	 *
	 * @param shopRsrcDto
	 * @param operateType
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月1日 上午11:37:30
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月1日    ChenYongxin      v1.0.0         create
	 */
	void operateResource(ShopRsrcPramDto shopRsrcPramDto) throws Exception;
	/**
	 * 查询商铺资源存在性
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopRsrcServcie.queryResourceExists
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
	 * 查询商铺资源名称
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopRsrcServcie.getResourceName
	 * @Description:
	 *
	 * @param resourceId
	 * 
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:LuJianPing
	 * @date:2015年8月5日 上午16:00:30
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月5日    LuJianPing      v1.0.0         create
	 */
	String getResourceName(Long resourceId) throws Exception;
	
}
