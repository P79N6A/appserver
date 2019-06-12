package com.idcq.appserver.dao.shop;

import com.idcq.appserver.dto.shop.ShopRsrcGroupDto;

public interface IShopRsrcGroupDao {
	
	/**
	 * 获取指定的商铺资源组
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	ShopRsrcGroupDto getShopRsrcGroupById(Long id) throws Exception;
	
	/**
	 * 获取指定商铺资源的ID
	 * 
	 * @param shopId	商铺ID
	 * @param groupId	资源组ID
	 * @return
	 * @throws Exception
	 */
	Long getGroupIdById(Long shopId ,Long groupId) throws Exception;
	
	/**
	 * 获取指定商铺的的资源组
	 * 
	 * @param shopId
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	ShopRsrcGroupDto getRsrcGroupById(Long shopId ,Long groupId) throws Exception;
	
	/**
	 * 根据订单ID获取指定的资源组
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	ShopRsrcGroupDto getRsrcGroupLimitOneByOrderId(String orderId) throws Exception;
	
}
