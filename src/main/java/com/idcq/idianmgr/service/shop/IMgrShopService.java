package com.idcq.idianmgr.service.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.idianmgr.dto.shop.CategoryDto;
import com.idcq.idianmgr.dto.shop.ShopBean;
import com.idcq.idianmgr.dto.shop.ShopBookSettingDto;
import com.idcq.idianmgr.dto.shop.ShopCashierParams;
import com.idcq.idianmgr.dto.shop.TechTypeDto;

public interface IMgrShopService {
	
	/**
	 * 设置商铺预约设置
	 * 
	 * @param bkSettingDto
	 * @return
	 * @throws Exception
	 */
	int setShopBookSetting(ShopBookSettingDto bkSettingDto) throws Exception;
	
	/**
	 * 操作服务分类接口
	 * @param categoryDto
	 */
	String operateCategory(CategoryDto categoryDto) throws Exception;

	/**
	 * 删除服务分类
	 * 停用服务分类
	 * @param categoryDto
	 */
	void delCategory(CategoryDto categoryDto) throws Exception;

	/**
	 * 新增或修改技师级别
	 * @param techTypeDto
	 */
	String updateTechType(TechTypeDto techTypeDto) throws Exception;

	/**
	 * 删除技师级别
	 * @param techTypeDto
	 */
	void delTechType(TechTypeDto techTypeDto) throws Exception;

	/**
	 * 分页查询技师级别列表
	 * 
	 * @param techTypeDto
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getTechTypes(TechTypeDto techTypeDto, Integer page,
			Integer pageSize) throws Exception;
	
	/**
	 * 新增商铺
	 * @param shopDto
	 * @throws Exception
	 */
	void saveShop(ShopDto shopDto, ShopBean shopBean) throws Exception;

	/**
	 * 新增、修改收银员
	 * @param shopCashier
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> optShopCashier(ShopCashierParams shopCashier) throws Exception;

	/**
	 * 删除收银员信息
	 * @param shopId
	 * @param cashierId
	 * @throws Exception
	 */
	void delShopCashier(Long shopId, Long cashierId) throws Exception;

	/**
	 * 更新商铺信息
	 * @param shopBean
	 */
	void updateShop(ShopBean shopBean)  throws Exception;

	/**
	 * 新增商铺与二级行业分类关联关系
	 * @param shopBean
	 */
	void addShopAndColumnRelation(ShopBean shopBean) throws Exception;

	/**
	 * 查询收银员列表
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	List<Map> getShopCashiers(long shopId) throws Exception;

	/**
	 * 查询会员
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	UserDto getUser(String mobile) throws Exception;

	UserDto getUserByMobile(String refeUser) throws Exception;

	UserDto getUserById(Long referUserId) throws Exception;

	boolean isExistSameShopName(String shopIdStr, String userIdStr,String shopName) throws Exception;

	Map<String, Integer> getObjCountByCondition(Long shopId, String startTime,String endTime, String searchObject);
}
