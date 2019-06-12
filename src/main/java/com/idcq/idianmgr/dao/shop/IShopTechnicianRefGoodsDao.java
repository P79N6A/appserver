package com.idcq.idianmgr.dao.shop;

import java.util.List;

import com.idcq.idianmgr.dto.shop.ShopTechnicianRefGoodsDto;

public interface IShopTechnicianRefGoodsDao {
	
	/**
	 * 批量新增技师和服务关联信息
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int addShopTechnicianRefGoodsBatch(List<ShopTechnicianRefGoodsDto> list) throws Exception;
	
	/**
	 * 删除技师和服务关联信息
	 * 
	 * @param groupGroupId
	 * @return
	 * @throws Exception
	 */
	int delShopTechnicianRefGoodsByGgId(Long groupGroupId) throws Exception;
	
}
