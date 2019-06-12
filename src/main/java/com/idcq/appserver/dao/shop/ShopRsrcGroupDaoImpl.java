package com.idcq.appserver.dao.shop;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.ShopRsrcGroupDto;

/**
 * 商铺资源dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月25日
 * @time 下午7:25:21
 */
@Repository
public class ShopRsrcGroupDaoImpl extends BaseDao<ShopRsrcGroupDto> implements IShopRsrcGroupDao{

	public ShopRsrcGroupDto getShopRsrcGroupById(Long id) throws Exception {
		return (ShopRsrcGroupDto)super.selectOne(generateStatement("getShopRsrcGroupById"), id);
	}

	public Long getGroupIdById(Long shopId,Long groupId) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("groupId",groupId);
		Long id2 = (Long)super.selectOne(generateStatement("getGroupIdById"), map);
		return id2 == null ? 0 : id2;
	}

	public ShopRsrcGroupDto getRsrcGroupById(Long shopId, Long groupId)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("groupId",groupId);
		return (ShopRsrcGroupDto)super.selectOne(generateStatement("getRsrcGroupById"), map);
	}

	public ShopRsrcGroupDto getRsrcGroupLimitOneByOrderId(String orderId)
			throws Exception {
		return (ShopRsrcGroupDto)super.selectOne(generateStatement("getRsrcGroupLimitOneByOrderId"), orderId);
	}
	
	

	
}
